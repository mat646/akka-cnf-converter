import akka.actor.{Actor, ActorSelection, ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import domain._
import io.circe.syntax._
import io.circe.parser.decode
import json.BooleanJsonProtocol
import akka.event.Logging

object ClientApp extends App {

  val config = ConfigFactory.load()
  val system: ActorSystem = ActorSystem("ClientApp", config.getConfig("ClientApp"))
  val client = system.actorOf(Props[Client], name = "ClientActor")

}

class Client extends Actor with BooleanJsonProtocol {

  val log = Logging(context.system, this)

  val remoteActor: ActorSelection = context.actorSelection("akka.tcp://ServerApp@127.0.0.1:2552/user/ServerActor")
  log.info("Remote server is at: " + remoteActor)

  val unreducedExpr: BooleanExpression = And(Not(Variable("A")), Or(Variable("B"), And(Variable("C"), False)))
  log.info("Sending: " + unreducedExpr.toString)

  remoteActor ! unreducedExpr.asJson.noSpaces

  override def receive: Receive = {
    case msg: String =>

      val expr = decode[BooleanExpression](msg)

      expr match {
        case Right(value) =>
          log.info(s"Got reduced expression: ${value.toString}")
        case Left(error) =>
          log.warning(s"Got malformed expression: ${error.toString}")
      }
  }
}
