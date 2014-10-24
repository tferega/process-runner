package com.ferega.procrun

import org.joda.time.DateTime

/** Represents a process that has ended.
 *
 *  @param name User-specified name given to this process
 *  @param command Command used to start the process
 *  @param arguments Arguments passed to command
 *  @param startedAt Process start time
 *  @param completedAt Process end time
 *  @param isKilled Specified weather the process was killed with `java.lang.Process.destroy()`
 *  @param stdOut Combined output to standard out
 *  @param stdErr Combined output to standard error
 *  @param exitCode Processes' exit code
 */
case class FinishedProcess(
    override val name: String,
    override val command: String,
    override val arguments: Seq[Any],
    startedAt: DateTime,
    completedAt: DateTime,
    isKilled: Boolean,
    stdOut: String,
    stdErr: String,
    exitCode: Int) extends Process {
  override val status: ProcessStatus = ProcessStatus.Stopped
  override val isRunning = false
  override val isStopped = true
}
