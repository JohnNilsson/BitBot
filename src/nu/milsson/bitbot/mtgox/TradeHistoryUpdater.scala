package nu.milsson.bitbot.mtgox

import nu.milsson.bitbot.storage.TradeHistory
import org.slf4j.LoggerFactory
import java.util.Date
import nu.milsson.bitbot.storage.Trade
import scala.util.Try

object TradeHistoryUpdater {
  val log = LoggerFactory.getLogger("TradeHistoryUpdater")
  val interestingTimeSpan = 1000L * 1000 * 60 * 60 * 24 * 7
  def apply() {
    val now = System.currentTimeMillis() * 1000;
    var last = Try(TradeHistory.lastUSDTrade).getOrElse(now - interestingTimeSpan)
    log.info("Begin trade fetch from {}", new Date(last / 1000))

    while (last < now) {
      log.info("Fetching trades since {}", new Date(last / 1000))

      val trades = MtGox.fetch(last) map { t => Trade(t.tid, t.amount, t.price) }

      if (trades.length == 0)
        return ;

      TradeHistory.addUSDTrades(trades)

      last = TradeHistory.lastUSDTrade
    }
  }
}