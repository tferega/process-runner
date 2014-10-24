package com.ferega.procrun

import java.io.File

import scala.collection.JavaConversions._
import scala.concurrent.duration.Duration

/** Main class used for starting a process. */
class ProcessRunner private (
    override val name: String,
    val workingDir: Option[File],
    override val command: String,
    override val arguments: Seq[Any]) extends Process {
  /** Constructs a new ProcessRunner with a default working directory and no
   *  arguments.
   *
   *  @param name User-specified name for this process
   *  @param command Command with which to start this process
   */
  def this(name: String, command: String) =
    this(name, None, command, Seq.empty)

  /** Constructs a new ProcessRunner with no arguments.
   *
   *  @param name User-specified name for this process
   *  @param workingDir Specifies the processes' working directory
   *  @param command Command with which to start this process
   */
  def this(name: String, workingDir: File, command: String) =
    this(name, Some(workingDir), command, Seq.empty)

  /** Constructs a new ProcessRunner with a default working directory.
   *
   *  @param name User-specified name for this process
   *  @param command Command with which to start this process
   *  @param arguments Arguments to the command
   */
  def this(name: String, command: String, arguments: Seq[Any]) =
    this(name, None, command, arguments)

  /** Constructs a new ProcessRunner.
   *
   *  @param name User-specified name for this process
   *  @param workingDir Specifies the processes' working directory
   *  @param command Command with which to start this process
   *  @param arguments Arguments to the command
   */
  def this(name: String, workingDir: File, command: String, arguments: Seq[Any]) =
    this(name, Some(workingDir), command, arguments)

  private val pb = new ProcessBuilder
  workingDir match {
    case Some(wd) => pb.directory(wd)
    case None     =>
  }
  pb.command(allArguments)

  override val status = ProcessStatus.Created
  override val isRunning = false
  override val isStopped = false

  /** Runs this process. */
  def run: RunningProcess = new RunningProcess(pb, name, command, arguments)

  /** Runs this process, and waits at most `timeout` for it to end.
   *
   *  @param timeout Specifies the `scala.concurrent.duration.Duration` to wait for the process to end.
   *
   *  This call will block until the process ends, or a timeout occurs. In the
   *  case of a timeout, the process is destroyed.
   *
   *  @return Result of running this process
   */
  def waitFor(timeout: Duration): FinishedProcess = run.waitFor(timeout)


  /** Runs this process, and waits a "small" amount of time for it to end.
   *
   *  This call will block until the process ends, or a "small" (500 ms) timeout
   *  occurs. In the case of a timeout, the process is destroyed.
   *
   *  Usually used for commands that should return "immediately" (`ls`, `ps` ...)
   *
   *  @return Result of running this process
   */
  def end: FinishedProcess = run.end
}
