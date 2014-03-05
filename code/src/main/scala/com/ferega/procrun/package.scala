package com.ferega

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext

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

  implicit class RichAny(l: Any) {
    def |(r: Any) =
      l match {
        case seq: Seq[_] => seq :+ r
        case _ => Seq(l, r)
      }
  }
}
