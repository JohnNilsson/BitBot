package nu.milsson.bitbot.storage

import org.mapdb.DBMaker
import java.io.File
import scala.collection.JavaConversions._
import org.mapdb.Fun
import org.mapdb.Serializer
import java.io.DataOutput
import java.io.DataInput
import nu.milsson.bitbot.mtgox.BTC
import nu.milsson.bitbot.mtgox.USD
import org.mapdb.BTreeKeySerializer.BasicKeySerializer
import org.mapdb.BTreeKeySerializer
import org.slf4j.LoggerFactory

object TradeHistory {
  val log = LoggerFactory.getLogger("TradeHistory")
  private[this] lazy val db = DBMaker
    .newFileDB(new File("bitbot.db"))
    .asyncWriteDisable() // Some weirdness regarding interrupts
    .compressionEnable()
    .closeOnJvmShutdown()
    .make()

  private[this] lazy val tradeUSD = db.getTreeMap[Long, Fun.Tuple2[Long, Long]]("trade.USD")

  private def trade2t2(trade: Trade) = Fun.t2(
    trade.amount.underlying.unscaledValue.longValue,
    trade.price.underlying.unscaledValue.longValue)

  def addUSDTrade(trade: Trade) {
    try {
      tradeUSD.put(trade.tid, trade2t2(trade))
    } finally {
      db.commit()
    }
  }

  def addUSDTrades(trades: Seq[Trade]) {
    log.info("Adding {} trades to db", trades.length)
    try {
      val tradeMap = trades.map(t => (t.tid, trade2t2(t))).toMap
      tradeUSD.putAll(tradeMap)
    } finally {
      db.commit()
    }
  }

  def lastUSDTrade = tradeUSD.lastKey()
}

case class Trade(val tid: Long, val amount: BigDecimal, val price: BigDecimal) extends Comparable[Trade] {
  def compareTo(other: Trade) = this.tid compareTo other.tid
}