import play.api._
import nu.milsson.bitbot.mtgox.tradehistory.TradeHistoryUpdater
import play.libs.Akka
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    Logger.info("Application has started")
    Akka.system.scheduler.schedule(0 seconds, 1 minute)(TradeHistoryUpdater.apply)
  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
  }

}