package com.ferega.procrun

import java.util.concurrent.TimeoutException

import org.joda.time.DateTime

import scala.collection.JavaConversions._
import scala.concurrent.{ Await, Future }
import scala.concurrent.duration._
import scala.util.{ Failure, Success }

class RunningProcess private[procrun] (pb: ProcessBuilder) {
  private case class ProcessResult(endedAt: DateTime, exitCode: Int)

  private val startedAt = DateTime.now
  private val p = pb.start

  private val waiter = Future {
    val exitCode = p.waitFor
    val endedAt = DateTime.now
    ProcessResult(endedAt, exitCode)
  }

  def waitFor(timeout: Duration) =
    tryt(Await.result(waiter, timeout)) match {
      case Success(pr)                  => report(pr, false)
      case Failure(e: TimeoutException) => kill
      case Failure(e)                   => throw new Exception("An error occured during process execution!", e)
    }

  def end = waitFor(Duration.Zero)

  private def kill = {
    p.destroy
    tryt(Await.result(waiter, 10 seconds)) match {
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
      stdOut      = "",
      errOut      = "",
      exitCode    = pr.exitCode)
}
