import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import domain.BooleanExpression
import io.circe.syntax._
import io.circe.parser.decode
import json.BooleanJsonProtocol
import akka.event.Logging

object ServerApp extends App {

  val config = ConfigFactory.load()
  val system: ActorSystem = ActorSystem("ServerApp", config.getConfig("ServerApp"))
  val server = system.actorOf(Props[Server], name = "ServerActor")

}

class Server extends Actor with BooleanJsonProtocol {

  val log = Logging(context.system, this)

  override def receive: Receive = {
    case msg: String =>

      val expr = decode[BooleanExpression](msg)

      expr match {
        case Right(value) =>
          log.info(s"Received: ${expr.toString}")
          sender ! BooleanExpression.convertToCNF(value).asJson.noSpaces
          log.info("Send back reduced expression")
        case Left(error) =>
          log.warning(s"Received invalid expression: ${error.toString}")
      }
  }
}
