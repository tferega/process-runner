package com.ferega.procrun

import java.io.File

import scala.collection.JavaConversions._
import scala.concurrent.duration.Duration

class ProcessRunner private (workingDir: Option[File], args: Seq[String]) {
  def this(workingDir: File, command: String) = this(Some(workingDir), Seq(command))
  def this(command: String)                   = this(None,             Seq(command))
  def this(workingDir: File, args: Seq[Any])  = this(Some(workingDir), args.map(_.toString))
  def this(args: Seq[Any])                    = this(None,             args.map(_.toString))

  private val pb = new ProcessBuilder
  workingDir match {
    case Some(wd) => pb.directory(wd)
    case None     =>
  }
  pb.command(args)

  def run: RunningProcess = new RunningProcess(pb)
  def waitFor(timeout: Duration): FinishedProcess = run.waitFor(timeout)
  def end: FinishedProcess = run.end
}
