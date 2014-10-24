package com.ferega.procrun

/** Specifies an execution status of a process. */
sealed trait ProcessStatus


object ProcessStatus {
  /** Used for processes which have not been started. */
  case object Created extends ProcessStatus

  /** Used for running processes. */
  case object Running extends ProcessStatus

  /** Used for stopped processes. */
  case object Stopped extends ProcessStatus
}
