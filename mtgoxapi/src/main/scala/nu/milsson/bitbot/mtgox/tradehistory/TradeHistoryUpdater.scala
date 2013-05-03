package nu.milsson.bitbot.mtgox.tradehistory

import scala.util.Try
import org.slf4j.LoggerFactory
import nu.milsson.bitbot.mtgox.MtGox
import java.util.concurrent.atomic.AtomicBoolean

object TradeHistoryUpdater {
  val log = LoggerFactory.getLogger("TradeHistoryUpdater")

  val interestingTimeSpan = 1000L * 1000 * 60 * 60 * 24 * 7

  def now = System.currentTimeMillis() * 1000

  def nextTradeToFetch = TradeHistory.lastUSDTrade getOrElse (now - interestingTimeSpan)

  def fetchTrades(since: Long) = MtGox.fetch(since) map { t => Trade(t.tid, t.amount, t.price) }

  def missingTrades = Stream.continually(fetchTrades(nextTradeToFetch)).takeWhile(_.length > 0)

  private[this] val running = new AtomicBoolean
  def apply() {
    if (!running.compareAndSet(false, true))
      throw new IllegalStateException("Allready running");
    try {
      log.info("Begin trade fetch")
      for (trades <- missingTrades)
        TradeHistory.addUSDTrades(trades)
    } catch {
      case e: Throwable => log.error("Fetch failed", e); throw e
    } finally {
      running.set(false)
    }
  }
}