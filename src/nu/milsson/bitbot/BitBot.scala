package nu.milsson.bitbot

import java.io.PrintStream
import nu.milsson.bitbot.mtgox._
import org.apache.commons.math3.stat.regression.SimpleRegression

object BitBot extends App {
  // Disable logging in java-websocket
  java.util.logging.LogManager.getLogManager().reset()
  java.util.logging.Logger.getGlobal().setLevel(java.util.logging.Level.OFF)

  val reg = new SimpleRegression()

  MtGox connect {
    case t: Ticker =>
      reg.addData(t.now, t.vwap.value.toDouble)
      println("%s %s".format(t.vwap, reg.getSlope()))
    //      println(t.json)

    case d: Depth =>
    //      println(d)
    //      println(d.json)

    case t: Trade =>
    //      println(t)
    //      println(t.json)
  }
}