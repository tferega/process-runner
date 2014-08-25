package com.ferega.procrun

import org.joda.time.DateTime

case class FinishedProcess(
    name: String,
    command: String,
    arguments: Seq[Any],
    startedAt: DateTime,
    completedAt: DateTime,
    isKilled: Boolean,
    stdOut: String,
    stdErr: String,
    exitCode: Int) extends Process {
  val status = ProcessStatus.Stopped
  val isRunning = false
  val isStopped = true
}
