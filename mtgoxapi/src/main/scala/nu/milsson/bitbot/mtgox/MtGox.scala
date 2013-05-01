package nu.milsson.bitbot.mtgox

import org.json.JSONObject
import org.slf4j.LoggerFactory
import nu.milsson.bitbot.mtgox.json._
import org.apache.commons.io.IOUtils
import java.net.URL
import java.util.Date
import scala.math.BigDecimal.RoundingMode

trait Currency {
  val scale: Int
  def round(value: BigDecimal) = value.setScale(this.scale, RoundingMode.HALF_EVEN)
  def apply(unscaled: Long) = round(BigDecimal(unscaled, scale))
}
object BTC extends Currency {
  val scale = 8
}

object USD extends Currency {
  val scale = 5
}

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

  def fetch(since: Long) = {
    log.info("Fetching trades since {}", new Date(since / 1000))
    parseFetch(IOUtils.toString(new URL("https://data.mtgox.com/api/2/BTCUSD/money/trades/fetch?since=" + since)))
  }

  def parseFetch(s: String) = {
    val data = new JSONObject(s).getJSONArray("data")
    val objects = (0 until data.length) map data.getJSONObject
    objects map { o => new Trade(o) }
  }
}