package nu.milsson.bitbot.mtgox.tradehistory

import scala.util.Try
import org.slf4j.LoggerFactory
import nu.milsson.bitbot.mtgox.MtGox

object TradeHistoryUpdater {
  val log = LoggerFactory.getLogger("TradeHistoryUpdater")

  val interestingTimeSpan = 1000L * 1000 * 60 * 60 * 24 * 7

  def now = System.currentTimeMillis() * 1000

  def nextTradeToFetch = Try(TradeHistory.lastUSDTrade).getOrElse(now - interestingTimeSpan)

  def fetchTrades(since: Long) = MtGox.fetch(since) map { t => Trade(t.tid, t.amount, t.price) }

  def missingTrades = Stream.continually(fetchTrades(nextTradeToFetch)).takeWhile(_.length > 0)

  def apply() {
    log.info("Begin trade fetch")
    for (trades <- missingTrades)
      TradeHistory.addUSDTrades(trades)
  }
}