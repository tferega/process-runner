package com.ferega.procrun

import java.io.File

import scala.collection.JavaConversions._
import scala.concurrent.duration.Duration

class ProcessRunner private (
    val workingDir: Option[File],
    val command: String,
    val arguments: Seq[Any]) extends Process {
  def this(workingDir: File, command: String)                      = this(Some(workingDir), command, Seq.empty)
  def this(command: String)                                        = this(None,             command, Seq.empty)
  def this(workingDir: File, command: String, arguments: Seq[Any]) = this(Some(workingDir), command, arguments)
  def this(command: String, arguments: Seq[Any])                   = this(None,             command, arguments)

  private val pb = new ProcessBuilder
  workingDir match {
    case Some(wd) => pb.directory(wd)
    case None     =>
  }
  pb.command(allArguments)

  val status = ProcessStatus.Created
  val isRunning = false
  val isStopped = false

  def run: RunningProcess = new RunningProcess(pb, command, arguments)
  def waitFor(timeout: Duration): FinishedProcess = run.waitFor(timeout)
  def end: FinishedProcess = run.end
}
