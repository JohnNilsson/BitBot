package nu.milsson.bitbot.mtgox.tradehistory

import nu.milsson.bitbot.mtgox._
import org.mapdb.DBMaker
import java.io.File
import scala.collection.JavaConversions._
import org.mapdb.Fun
import org.slf4j.LoggerFactory
import scala.util.Try

object TradeHistory {
  val log = LoggerFactory.getLogger("TradeHistory")
  private[this] lazy val db = DBMaker
    .newFileDB(new File("bitbot.db"))
    .asyncWriteDisable() // Some weirdness regarding interrupts
    .closeOnJvmShutdown()
    .make()

  private[this] lazy val tradeUSD = db.getTreeMap[Long, Fun.Tuple2[Long, Long]]("trade.USD")

  private def trade2t2(trade: Trade) = Fun.t2(
    trade.amount.underlying.unscaledValue.longValue,
    trade.price.underlying.unscaledValue.longValue)

  private def t22trade(tid: Long, t: Fun.Tuple2[Long, Long]) = Trade(tid = tid, amount = BTC(t.a), price = USD(t.b))

  def addUSDTrades(trades: Seq[Trade]) {
    log.info("Adding {} trades to db", trades.length)
    try {
      val tradeMap = trades.map(t => (t.tid, trade2t2(t))).toMap
      tradeUSD.putAll(tradeMap)
      db.commit()
    } finally {
      db.rollback()
    }
  }

  def lastUSDTrade = try { Some(tradeUSD.lastKey()) } catch { case _: NoSuchElementException => None }

  def getUSDTrades(since: Long, until: Long) = tradeUSD.subMap(since, until).map(t => t22trade(t._1, t._2))

}

case class Trade(val tid: Long, val amount: BigDecimal, val price: BigDecimal) extends Comparable[Trade] {
  def compareTo(other: Trade) = this.tid compareTo other.tid
}