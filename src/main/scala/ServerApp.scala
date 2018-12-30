import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import domain.BooleanExpression
import json.BooleanJsonProtocol
import spray.json._

object ServerApp extends App {

  val config = ConfigFactory.load()
  val system: ActorSystem = ActorSystem("ServerApp", config.getConfig("ServerApp"))
  val server = system.actorOf(Props[Server], name = "ServerActor")

}

class Server extends Actor with BooleanJsonProtocol {
  override def receive: Receive = {
    case msg: JsValue =>
      val expr = msg.convertTo[BooleanExpression]
      println("Received: " + expr.toString)
      sender ! BooleanExpression.convertToCNF(expr).toJson
  }
}
