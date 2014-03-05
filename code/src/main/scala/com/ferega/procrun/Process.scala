package com.ferega.procrun

trait Process {
  def status: ProcessStatus
  def isRunning: Boolean
  def isStopped: Boolean

  val args: Seq[String]

  lazy val command = args.headOption getOrElse ""
}
