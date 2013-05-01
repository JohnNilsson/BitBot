package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.Json
import nu.milsson.bitbot.mtgox.tradehistory.TradeHistory
import nu.milsson.bitbot.mtgox.tradehistory.Trade
import scala.collection.JavaConversions._
import nu.milsson.bitbot.mtgox._
import scala.math.BigDecimal.RoundingMode

object Application extends Controller {
  def index = Action {
    Ok(views.html.graph())
  }

  def fetchTrades(since: Long, until: Long, step: Long): Stream[Iterable[Trade]] =
    if (until <= since) Stream.Empty
    else TradeHistory.getUSDTrades(since, (since + step) min until) #:: fetchTrades(since + step, until, step)

  def fillMissing[T](filler: T, s: Iterable[Option[T]]): Stream[T] = {
    if (s.isEmpty) Stream.Empty
    else {
      val next = s.head getOrElse filler
      next #:: fillMissing(next, s.tail)
    }
  }

  def vwap(trades: Iterable[Trade]): Option[BigDecimal] =
    if (trades.isEmpty) None
    else Some(USD.round(trades.map(t => t.amount * t.price).sum / trades.map(t => t.amount).sum))

  def trades = Action { req =>
    val since = req.getQueryString("start").get.toLong * 1000
    val until = req.getQueryString("stop").get.toLong * 1000
    val step = req.getQueryString("step").get.toLong * 1000

    Ok(Json.toJson(fillMissing(BigDecimal(0), fetchTrades(since, until, step) map vwap)))
  }
}