package com.ferega

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import scala.util.{ Failure, Success, Try }

package object procrun {
  implicit val ec = ExecutionContext.fromExecutor(java.util.concurrent.Executors.newCachedThreadPool())

  val ReasonableTimeout = 10 seconds

  def tryo[T](f: => T): Option[T] =
    try {
      Some(f)
    } catch {
      case e: Exception => None
    }

  def tryt[T](f: => T): Try[T] =
    try {
      Success(f)
    } catch {
      case e: Exception => Failure(e)
    }
}