package nu.milsson.bitbot.mtgox

import org.json.JSONObject

trait IOCallback extends io.socket.IOCallback {
  def onMessage(json: JSONObject, ack: io.socket.IOAcknowledge) {
  }

  def on(event: String, ack: io.socket.IOAcknowledge, args: AnyRef*) {
  }

  def onConnect() {
  }

  def onDisconnect() {
  }

  def onError(ex: io.socket.SocketIOException) {
    ex.printStackTrace()
    System.exit(-1)
  }

  def onMessage(msg: String, ack: io.socket.IOAcknowledge) {
  }
}