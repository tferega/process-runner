package com.ferega.procrun

import java.io.File

import scala.collection.JavaConversions._
import scala.concurrent.duration.Duration

class ProcessRunner private (
    val name: String,
    val workingDir: Option[File],
    val command: String,
    val arguments: Seq[Any]) extends Process {
  def this(name: String, workingDir: File, command: String)                      = this(name, Some(workingDir), command, Seq.empty)
  def this(name: String, command: String)                                        = this(name, None,             command, Seq.empty)
  def this(name: String, workingDir: File, command: String, arguments: Seq[Any]) = this(name, Some(workingDir), command, arguments)
  def this(name: String, command: String, arguments: Seq[Any])                   = this(name, None,             command, arguments)

  private val pb = new ProcessBuilder
  workingDir match {
    case Some(wd) => pb.directory(wd)
    case None     =>
  }
  pb.command(allArguments)

  val status = ProcessStatus.Created
  val isRunning = false
  val isStopped = false

  def run: RunningProcess = new RunningProcess(pb, name, command, arguments)
  def waitFor(timeout: Duration): FinishedProcess = run.waitFor(timeout)
  def end: FinishedProcess = run.end
}
