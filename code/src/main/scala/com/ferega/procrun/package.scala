package com.ferega

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext

/** Provides classes and methods for native process manipulation.
 *
 *  Supported actions are:
 *    - Uses `java.lang.ProcessBuilder` in the background
 *    - Process management is asynchronous
 *    - Collects process output (both stdout and stderr), which can be fetched during execution
 *    - Processes can be destroyed
 *    - Can wait for a process to finish (with a timeout)
 *
 *  === Examples ===
 *
 *  Listing folder contents:
 *  {{{
 *  // Runs ls, in folder specified by `folder`, and immediately returns its
 *  // output.
 *  import com.ferega.procrun._
 *
 *  def listFolder(folder: File) =
 *    new ProcessRunner("folder listing", folder, "ls").end
 *  }}}
 *
 *  Generating a calendar:
 *  {{{
 *  // Runs cal with specified year and month and returns generated calendar if
 *  // successful (from std out). Otherwise, returns error description (from
 *  // std err).
 *  import com.ferega.procrun._
 *
 *  def getCalendar(year: Int, month: Int): String = {
 *    val result = new ProcessRunner("calendar", "cal", "-m" | month | year).end
 *    if (result.exitCode == 0) {
 *      result.stdOut.trim
 *    } else {
 *      "Error: " + result.stdErr.split("\n")(0)  // Just the first line of output
 *    }
 *  }
 *  }}}
 */
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


  /** Implicit that allows simple building of Şeq[Any].
   *
   *  Usage:
   *  {{{
   *  ("asdf" | 44 | '#') == Seq("asdf", 44, '#')
   *  }}}
   */
  implicit class RichAnyToSeq(l: Any) {
    def |(r: Any) =
      l match {
        case seq: Seq[_] => seq :+ r
        case _ => Seq(l, r)
      }
  }
}
