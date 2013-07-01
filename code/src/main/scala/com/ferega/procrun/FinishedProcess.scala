package com.ferega.procrun

import org.joda.time.DateTime

case class FinishedProcess(
    commandLine: String,
    startedAt: DateTime,
    completedAt: DateTime,
    isKilled: Boolean,
    stdOut: String,
    errOut: String,
    exitCode: Int) {
}