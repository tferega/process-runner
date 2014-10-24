package com.ferega.procrun

/** Base trait representing a process.  */
trait Process {
  /** User-defined name of this process. */
  def name: String

  /** Specifies status of this process. */
  def status: ProcessStatus

  /** Test weather this process is currently running.
    * @return `true` if the process is running, `false` otherwise.
    */
  def isRunning: Boolean

  /** Test weather this process has been stopped.
    * @return `true` if the process has been stopped, `false` otherwise.
    */
  def isStopped: Boolean

  /** Command used to start this process. */
  def command: String

  /** Arguments for the command. */
  def arguments: Seq[Any]

  /** Command and arguments combined into a single `Seq`. */
  def allArguments: Seq[String] = command +: arguments.map(_.toString)

  /** Command and arguments combined into a single command line string. */
  def commandLine: String = allArguments.mkString(" ")
}
