package nu.milsson.bitbot.mtgox.json

import org.json.JSONObject
import java.util.Date
import scala.math.BigDecimal.int2bigDecimal
import nu.milsson.bitbot.mtgox._

sealed trait MtGoxEvent

class Depth(val json: JSONObject) extends MtGoxEvent {
  def volume = BTC(json.getLong("volume_int"))
  def price = USD(json.getLong("price_int"))
  def now = json.getLong("now")
  def total_volume = BTC(json.getLong("total_volume_int"))
  def item = json.getString("item")
  def `type` = json.getString("type_str")
  def currency = json.getString("currency")
}

class TickerData(val json: JSONObject, currency: Currency) {
  def value = currency(json.getLong("value_int"))
  def display = json.getString("display")
  def display_short = json.getString("display_short")
  def currency = json.getString("currency")
}

class Ticker(val json: JSONObject) extends MtGoxEvent {
  private[this] def sect(s: String, currency: Currency) = new TickerData(json.getJSONObject(s), currency)
  lazy val vwap = sect("vwap", USD)
  lazy val last = sect("last", USD)
  lazy val last_orig = sect("last_orig", USD)
  lazy val buy = sect("buy", USD)
  lazy val last_local = sect("last_local", USD)
  lazy val vol = sect("vol", BTC)
  def now = json.getLong("now")
  lazy val sell = sect("sell", USD)
  def item = json.getString("item")
  lazy val last_all = sect("last_all", USD)
  lazy val high = sect("high", USD)
  lazy val low = sect("low", USD)
  lazy val avg = sect("avg", USD)
}

class Trade(val json: JSONObject) extends MtGoxEvent {
  def amount = BTC(json.getLong("amount_int"))
  def price = USD(json.getLong("price_int"))
  def primary = json.getString("primary")
  def item = json.getString("item")
  def price_currency = json.getString("price_currency")
  def properties = json.getString("properties")
  def tid = json.getLong("tid")
  def date = new Date(json.getInt("date"))
  def `type` = json.getString("type")
  def trade_type = json.getString("trade_type")
}