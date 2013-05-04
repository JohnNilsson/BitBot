package nu.milsson.bitbot.mtgox.tradehistory

import scala.collection.immutable.Stream.consWrapper

import org.slf4j.LoggerFactory

import com.higherfrequencytrading.chronicle.Excerpt
import com.higherfrequencytrading.chronicle.impl.IndexedChronicle

import nu.milsson.bitbot.mtgox.BTC
import nu.milsson.bitbot.mtgox.USD

case class Trade(val tid: Long, val amount: BigDecimal, val price: BigDecimal) extends Comparable[Trade] {
  def compareTo(other: Trade) = this.tid compareTo other.tid
}

object TradeHistory {
  private[this] val log = LoggerFactory.getLogger("TradeHistory")

  private[this] lazy val db = {
    val db = new IndexedChronicle("bitbot.db")
    //TODO: Let Akka handle both serialization of writes, and robust shutdown
    Runtime.getRuntime().addShutdownHook(new Thread {
      override def run() {
        db.close()
      }
    })
    db
  }

  private[this] lazy val excerpt = db.createExcerpt()

  private[this] implicit class TradeExceprt(val e: Excerpt) extends AnyVal {
    def writeTrade(t: Trade) {
      e.startExcerpt(8 + 8 + 8)
      e.writeLong(t.tid)
      e.writeLong(t.amount.underlying.unscaledValue.longValue)
      e.writeLong(t.price.underlying.unscaledValue.longValue)
      e.finish()
    }

    def readTradeId() = e.readLong(0)

    def readTradeData(id: Long) = Trade(id, BTC(e.readLong(8)), USD(e.readLong(16)))

    def readTrade() = readTradeData(readTradeId)

  }

  def addUSDTrades(trades: Seq[Trade]): Unit = excerpt.synchronized {
    log.info("Adding {} trades to db", trades.length)
    trades.foreach(excerpt.writeTrade)
  }

  def lastUSDTrade = excerpt.synchronized {
    if (db.size > 0) {
      excerpt.index(db.size() - 1)
      Some(excerpt.readTradeId)
    } else {
      None
    }
  }

  // TODO: Optimize, cache and index
  private[this] def streamExcerptTrades(since: Long, until: Long, e: Excerpt): Stream[Trade] =
    {
      if (e.nextIndex) {
        val tid = e.readTradeId
        if (tid < since) streamExcerptTrades(since, until, e)
        else if (tid >= until)
          Stream.empty
        else
          e.readTradeData(tid) #:: streamExcerptTrades(since, until, e)
      } else
        Stream.empty
    }

  def getUSDTrades(since: Long, until: Long) = streamExcerptTrades(since, until, db.createExcerpt())
}