package nu.milsson.bitbot.mtgox

import org.json.JSONObject
import org.slf4j.LoggerFactory

object MtGox {
  val log = LoggerFactory.getLogger("MtGox")

  val TickerChannel = "d5f06780-30a8-4a48-a2f8-7ed181b4a13f";
  val TradesChannel = "dbf1dee9-4f2e-4a08-8cb7-748919a71b21";
  val DepthChannel = "24e67e0d-1cad-4cc0-9e7a-f8523ef460fe";

  def connect(handler: MtGoxEvent => Unit) {
    val socket = new io.socket.SocketIO("http://socketio.mtgox.com/mtgox")
    socket connect new IOCallback {
      override def onMessage(json: JSONObject, ack: io.socket.IOAcknowledge) {
        json.getString("channel") match {
          case TickerChannel if (json.has("ticker")) => handler(new Ticker(json.getJSONObject("ticker")))
          case DepthChannel if (json.has("depth"))   => handler(new Depth(json.getJSONObject("depth")))
          case TradesChannel if (json.has("trade"))  => handler(new Trade(json.getJSONObject("trade")))
          case _                                     => log.warn("Unhandled {}", json)
        }
      }
    }
  }
}
