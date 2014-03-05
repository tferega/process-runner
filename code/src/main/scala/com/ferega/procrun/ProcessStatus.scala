package com.ferega.procrun

sealed trait ProcessStatus
object ProcessStatus {
  case object Created extends ProcessStatus
  case object Running extends ProcessStatus
  case object Stopped extends ProcessStatus
}
