//package nu.milsson.bitbot
//
//import org.scalatest._
//import org.scalatest.matchers.ShouldMatchers
//import nu.milsson.bitbot.storage.Trade
//import nu.milsson.bitbot.mtgox.BTC
//import nu.milsson.bitbot.mtgox.USD
//import nu.milsson.bitbot.storage.TradeSerializer
//import java.io.DataOutput
//import java.io.DataOutputStream
//import java.io.ByteArrayOutputStream
//import java.io.ByteArrayInputStream
//import java.io.DataInputStream
//
//class SerializerTests extends FreeSpec with ShouldMatchers {
//  "Serialize" - {
//    "Serialize Trade" in {
//      val trade = Trade(Long.MaxValue - 1, BTC(Long.MaxValue - 2), USD(Long.MaxValue - 3))
//      var ser = new TradeSerializer()
//
//      var outData = new ByteArrayOutputStream(8 * 3)
//      ser.serialize(new DataOutputStream(outData), trade)
//      var inData = new ByteArrayInputStream(outData.toByteArray())
//      val trade2 = ser.deserialize(new DataInputStream(inData), 1)
//
//      trade2 should be === trade
//    }
//  }
//}