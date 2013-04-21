package nu.milsson.bitbot.mtgox

import org.json.JSONObject
import java.util.Date

trait MtGoxEvent

object CurrencyDivisor {
  val USD = 100000
  val BTC = 100000000
}

class Depth(val json: JSONObject) extends MtGoxEvent {
  def volume = BigDecimal(json.getInt("volume_int")) / CurrencyDivisor.BTC
  def price = BigDecimal(json.getInt("price_int")) / CurrencyDivisor.USD
  def now = json.getLong("now")
  def total_volume = BigDecimal(json.getInt("total_volume_int")) / CurrencyDivisor.BTC
  def item = json.getString("item")
  def `type` = json.getString("type_str")
  def currency = json.getString("currency")

  override def toString() = "depth [volume: %s, price: %s, total_volume: %s]".format(volume, price, total_volume)
}

class TickerData(val json: JSONObject, div: Int) extends MtGoxEvent {
  def value = BigDecimal(json.getLong("value_int")) / div
  def display = json.getString("display")
  def display_short = json.getString("display_short")
  def currency = json.getString("currency")

  override def toString() = "%s: %s".format(currency, value)
}

class Ticker(val json: JSONObject) extends MtGoxEvent {
  private[this] def sect(s: String, div: Int) = new TickerData(json.getJSONObject(s), div)
  lazy val vwap = sect("vwap", CurrencyDivisor.USD)
  lazy val last = sect("last", CurrencyDivisor.USD)
  lazy val last_orig = sect("last_orig", CurrencyDivisor.USD)
  lazy val buy = sect("buy", CurrencyDivisor.USD)
  lazy val last_local = sect("last_local", CurrencyDivisor.USD)
  lazy val vol = sect("vol", CurrencyDivisor.BTC)
  def now = json.getLong("now")
  lazy val sell = sect("sell", CurrencyDivisor.USD)
  def item = json.getString("item")
  lazy val last_all = sect("last_all", CurrencyDivisor.USD)
  lazy val high = sect("high", CurrencyDivisor.USD)
  lazy val low = sect("low", CurrencyDivisor.USD)
  lazy val avg = sect("avg", CurrencyDivisor.USD)

  override def toString() = "ticker [buy: %s, sell: %s, high: %s, avg: %s, low: %s]".format(buy, sell, high, avg, low)
}

class Trade(val json: JSONObject) extends MtGoxEvent {
  def amount = BigDecimal(json.getInt("amount_int")) / CurrencyDivisor.BTC
  def price = BigDecimal(json.getDouble("price_int")) / CurrencyDivisor.USD
  def primary = json.getString("primary")
  def item = json.getString("item")
  def price_currency = json.getString("price_currency")
  def properties = json.getString("properties")
  def tid = json.getLong("tid")
  def date = new Date(json.getInt("date"))
  def `type` = json.getString("type")
  def trade_type = json.getString("trade_type")

  override def toString() = "trade [amount: %s, price: %s %s]".format(amount, price, price_currency)
}