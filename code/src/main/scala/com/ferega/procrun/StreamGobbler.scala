package com.ferega.procrun

import java.io.InputStream
import scala.concurrent.{ Await, Future }
import scala.util.{ Failure, Success }
import java.io.IOException

class StreamGobbler(is: InputStream) {
  private case object Lock

  var body = new StringBuilder

  val gobbler = Future {
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

  def waitFor = {
    tryt(Await.result(gobbler, ReasonableTimeout)) match {
      case Success(_) => body.result
      case Failure(e) => throw new Exception("An error occured while reading process output!", e)
    }
  }

  def bodySoFar =
    Lock.synchronized {
      body.result
    }
}