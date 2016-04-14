package com.ferega.procrun

import java.io.InputStream
import scala.concurrent.{ Await, ExecutionContext, Future }
import scala.util.{ Failure, Success, Try }

/** Consumes the specified `InputStream` until it stops producing output.
 *
 *  Also, allows to peek at consumed output at any time.
 */
private class StreamGobbler(is: InputStream)(implicit ev: ExecutionContext) {
  private case object Lock

  private val body = new StringBuilder

  private val gobbler = Future {
    var end = false
    val ba: Array[Byte] = new Array(128)

    while (!end) {
      val count = is.read(ba)
      if (count > 0) {
        val ca = ba.map(_.toChar)

        Lock.synchronized {
          body.appendAll(ca, 0, count)
        }
      } else {
        end = true
      }
    }
  }

  /** Waits a "reasonable" amount of time for the stream to stop producing
   *  output.
   *
   *  @return Consumed output
   */
  def waitFor = {
    Try(Await.result(gobbler, ReasonableTimeout)) match {
      case Success(_) => body.result
      case Failure(e) => throw new Exception("An error occurred while reading process output!", e)
    }
  }

  /** Returns output consumed so far. */
  def bodySoFar =
    Lock.synchronized {
      body.result
    }
}
