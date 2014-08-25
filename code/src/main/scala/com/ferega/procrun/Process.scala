package com.ferega.procrun

trait Process {
  def name: String

  def status: ProcessStatus
  def isRunning: Boolean
  def isStopped: Boolean

  def command: String
  def arguments: Seq[Any]

  def allArguments: Seq[String] = command +: arguments.map(_.toString)
  def commandLine: String = allArguments.mkString(" ")
}
