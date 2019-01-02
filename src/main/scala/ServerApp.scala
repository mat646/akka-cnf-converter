import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import domain.BooleanExpression
import json.BooleanJsonProtocol
import io.circe.syntax._
import io.circe.parser.decode

object ServerApp extends App {

  val config = ConfigFactory.load()
  val system: ActorSystem = ActorSystem("ServerApp", config.getConfig("ServerApp"))
  val server = system.actorOf(Props[Server], name = "ServerActor")

}

class Server extends Actor with BooleanJsonProtocol {
  override def receive: Receive = {
    case msg: String =>
      val expr = decode[BooleanExpression](msg)

      expr match {
        case Right(value) =>
          println("Received: " + expr.toString)
          sender ! BooleanExpression.convertToCNF(value).asJson.noSpaces
        case Left(error) =>
          println("Received invalid expression: " + error.toString)
      }

  }
}
