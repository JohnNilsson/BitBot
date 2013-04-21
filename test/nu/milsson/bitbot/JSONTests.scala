package nu.milsson.bitbot

import org.scalatest._
import org.json.JSONObject
import nu.milsson.bitbot.mtgox.Depth
import org.scalatest.matchers.ShouldMatchers
import nu.milsson.bitbot.mtgox.Ticker
import nu.milsson.bitbot.mtgox.Trade

class JSONTests extends FreeSpec with ShouldMatchers {
  "Parse MtGox events" - {
    "Parse Depth" in {
      val msg = """{
          "volume_int":"-160000000",
          "price":"116.87727",
          "now":"1366502759221601",
          "price_int":"11687727",
          "total_volume_int":"100000000",
          "item":"BTC",
          "volume":"-1.6",
          "type_str":"bid",
          "type":2,
          "currency":"USD"}"""

      val v = new Depth(new JSONObject(msg))

      v.volume should be === (BigDecimal("-1.6"))
      v.price should be === (BigDecimal("116.87727"))
      v.total_volume should be === (BigDecimal(1))
      v.item should be === ("BTC")
      v.`type` should be === ("bid")
      v.currency should be === ("USD")
    }

    "Parse Ticker" in {
      val msg = """{
        "vwap":{"value_int":"12455160","value":"124.55160","display":"$124.55160","display_short":"$124.55","currency":"USD"},
        "last":{"value_int":"12810001","value":"128.10001","display":"$128.10001","display_short":"$128.10","currency":"USD"},
        "last_orig":{"value_int":"12810001","value":"128.10001","display":"$128.10001","display_short":"$128.10","currency":"USD"},
        "buy":{"value_int":"12810001","value":"128.10001","display":"$128.10001","display_short":"$128.10","currency":"USD"},
        "last_local":{"value_int":"12810001","value":"128.10001","display":"$128.10001","display_short":"$128.10","currency":"USD"},
        "vol":{"value_int":"7378923189610","value":"73789.23189610","display":"73,789.23189610 BTC","display_short":"73,789.23 BTC","currency":"BTC"},
        "now":"1366505062575767",
        "sell":{"value_int":"12939999","value":"129.39999","display":"$129.39999","display_short":"$129.40","currency":"USD"},
        "item":"BTC",
        "last_all":{"value_int":"12810001","value":"128.10001","display":"$128.10001","display_short":"$128.10","currency":"USD"},
        "high":{"value_int":"13200000","value":"132.00000","display":"$132.00000","display_short":"$132.00","currency":"USD"},
        "low":{"value_int":"11500000","value":"115.00000","display":"$115.00000","display_short":"$115.00","currency":"USD"},
        "avg":{"value_int":"12371141","value":"123.71141","display":"$123.71141","display_short":"$123.71","currency":"USD"}}"""

      val v = new Ticker(new JSONObject(msg))
      v.vwap.value should be === (BigDecimal("124.55160"))
      v.last.value should be === (BigDecimal("128.10001"))
      v.last_orig.value should be === (BigDecimal("128.10001"))
      v.buy.value should be === (BigDecimal("128.10001"))
      v.last_local.value should be === (BigDecimal("128.10001"))
      v.vol.value should be === (BigDecimal("73789.23189610"))
      v.sell.value should be === (BigDecimal("129.39999"))
      v.last_all.value should be === (BigDecimal("128.10001"))
      v.high.value should be === (BigDecimal("132.00000"))
      v.low.value should be === (BigDecimal("115.00000"))
      v.avg.value should be === (BigDecimal("123.71141"))

    }

    "Parse Trade" in {
      val msg = """{
        "amount":2.75,
        "price":128.00001,
        "price_int":"12800001",
        "primary":"Y",
        "item":"BTC",
        "price_currency":"USD",
        "properties":"market",
        "tid":"1366505047526318",
        "date":1366505047,
        "type":"trade",
        "trade_type":"ask",
        "amount_int":"275000000"}"""

      val v = new Trade(new JSONObject(msg))

      v.amount should be === BigDecimal("2.75")
      v.price should be === BigDecimal("128.00001")
      v.price_currency should be === "USD"

    }

  }
}