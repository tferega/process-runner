package com.ferega.procrun

import java.util.concurrent.TimeoutException

import org.joda.time.DateTime

import scala.collection.JavaConversions._
import scala.concurrent.{ Await, Future }
import scala.concurrent.duration._
import scala.util.{ Failure, Success }

class RunningProcess private[procrun] (pb: ProcessBuilder) {
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

  def waitFor(timeout: Duration) =
    tryt(Await.result(waiter, timeout)) match {
      case Success(pr)                  => report(pr, false)
      case Failure(e: TimeoutException) => kill
      case Failure(e)                   => throw new Exception("An error occured during process execution!", e)
    }

  def end = waitFor(Duration.Zero)

  def stdOutSoFar = outGobbler.bodySoFar
  def stdErrSoFar = errGobbler.bodySoFar

  private def kill = {
    p.destroy
    tryt(Await.result(waiter, ReasonableTimeout)) match {
      case Success(pr)                  => report(pr, true)
      case Failure(e: TimeoutException) => throw new Exception("The process refused to die in a timely manner!", e)
      case Failure(e)                   => throw new Exception("An error occured during process execution!", e)
    }
  }

  private def report(pr: ProcessResult, isKilled: Boolean) =
    FinishedProcess(
      commandLine = pb.command.mkString(" "),
      startedAt   = startedAt,
      completedAt = pr.endedAt,
      isKilled    = isKilled,
      stdOut      = pr.stdOut,
      stdErr      = pr.stdErr,
      exitCode    = pr.exitCode)
}
