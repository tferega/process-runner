package com.ferega.procrun

import java.util.concurrent.TimeoutException
import org.joda.time.DateTime
import scala.concurrent.{ Await, Future }
import scala.concurrent.duration._
import scala.util.{ Failure, Success, Try }

/** Represents a running process. */
class RunningProcess private[procrun] (
    pb: ProcessBuilder,
    override val name: String,
    override val command: String,
    override val arguments: Seq[Any]) extends Process {
  private case class ProcessResult(endedAt: DateTime, exitCode: Int, stdOut: String, stdErr: String)

  private val startedAt = DateTime.now
  private val p = pb.start
  private val outGobbler = new StreamGobbler(p.getInputStream)
  private val errGobbler = new StreamGobbler(p.getErrorStream)

  private val waiter = Future {
    val exitCode = p.waitFor
    val stdOut = outGobbler.waitFor
    val stdErr = errGobbler.waitFor
    val endedAt = DateTime.now
    ProcessResult(endedAt, exitCode, stdOut, stdErr)
  }

  override def status  = if (waiter.isCompleted) ProcessStatus.Stopped else ProcessStatus.Running
  override def isRunning = !waiter.isCompleted
  override def isStopped = waiter.isCompleted

  /** Waits at most `timeout` for this process to end.
   *
   *  @param timeout Specifies the `scala.concurrent.duration.Duration` to wait for the process to end.
   *
   *  This call will block until the process ends, or a timeout occurs. In the
   *  case of a timeout, the process is destroyed.
   *
   *  @return Result of running this process
   */
  def waitFor(timeout: Duration): FinishedProcess =
    Try(Await.result(waiter, timeout)) match {
      case Success(pr)                  => report(pr, isKilled = false)
      case Failure(e: TimeoutException) => kill
      case Failure(e)                   => throw new Exception("An error occurred during process execution!", e)
    }

  /** Waits a "small" amount of time for this process to end.
   *
   *  This call will block until the process ends, or a "small" (500 ms) timeout
   *  occurs. In the case of a timeout, the process is destroyed.
   *
   *  @return Result of running this process
   */
  def end = waitFor(SmallTimeout)

  /** Gets the standard output this process has produced so far.
   *
   *  @return Standard output of this process
   */
  def stdOutSoFar = outGobbler.bodySoFar

  /** Gets the standard error this process has produced so far.
   *
   *  @return Standard error of this process
   */
  def stdErrSoFar = errGobbler.bodySoFar

  private def kill = {
    p.destroy()
    Try(Await.result(waiter, ReasonableTimeout)) match {
      case Success(pr)                  => report(pr, isKilled = true)
      case Failure(e: TimeoutException) => throw new Exception("The process refused to die in a timely manner!", e)
      case Failure(e)                   => throw new Exception("An error occurred during process execution!", e)
    }
  }

  private def report(pr: ProcessResult, isKilled: Boolean) =
    FinishedProcess(
      name        = name,
      command     = command,
      arguments   = arguments,
      startedAt   = startedAt,
      completedAt = pr.endedAt,
      isKilled    = isKilled,
      stdOut      = pr.stdOut,
      stdErr      = pr.stdErr,
      exitCode    = pr.exitCode)
}
