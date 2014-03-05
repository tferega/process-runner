package com.ferega.procrun

import java.io.InputStream

import scala.concurrent.{ Await, Future }
import scala.util.{ Try, Failure, Success }

class StreamGobbler(is: InputStream) {
  private case object Lock

  private var body = new StringBuilder

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

  def waitFor = {
    Try(Await.result(gobbler, ReasonableTimeout)) match {
      case Success(_) => body.result
      case Failure(e) => throw new Exception("An error occured while reading process output!", e)
    }
  }

  def bodySoFar =
    Lock.synchronized {
      body.result
    }
}
