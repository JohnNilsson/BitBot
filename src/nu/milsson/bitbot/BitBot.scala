package nu.milsson.bitbot

import scala.collection.JavaConversions._
import com.xeiam.xchange._
import com.xeiam.xchange.currency._
import com.xeiam.xchange.mtgox.v1._

object BitBot extends App {
  val mtGox = ExchangeFactory.INSTANCE.createExchange(classOf[MtGoxExchange].getName)
  val mdService = mtGox.getPollingMarketDataService
  val orderBook = mdService.getPartialOrderBook(Currencies.BTC, Currencies.USD)
  orderBook.getBids.foreach(println)
}