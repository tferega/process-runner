package com.ferega

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import scala.util.{ Failure, Success, Try }

package object procrun {
  private[procrun] implicit val ec = ExecutionContext.fromExecutor(java.util.concurrent.Executors.newCachedThreadPool())

  private[procrun] val ReasonableTimeout = 10  seconds
  private[procrun] val SmallTimeout      = 500 millis

  private[procrun] def tryo[T](f: => T): Option[T] =
    try {
      Some(f)
    } catch {
      case e: Exception => None
    }

  private[procrun] def tryt[T](f: => T): Try[T] =
    try {
      Success(f)
    } catch {
      case e: Exception => Failure(e)
    }
}