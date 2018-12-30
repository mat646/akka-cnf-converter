import akka.actor.{Actor, ActorSelection, ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import domain._
import json.BooleanJsonProtocol
import spray.json._

object ClientApp extends App {

  val config = ConfigFactory.load()
  val system: ActorSystem = ActorSystem("ClientApp", config.getConfig("ClientApp"))
  val client = system.actorOf(Props[Client], name = "ClientActor")

}

class Client extends Actor with BooleanJsonProtocol {

  val remoteActor: ActorSelection = context.actorSelection("akka.tcp://ServerApp@127.0.0.1:2552/user/ServerActor")
  println("Remote server is at: " + remoteActor)

  val unreducedExpr: BooleanExpression = And(Not(Variable("A")), Or(Variable("B"), And(Variable("C"), False)))
  println("Sending: " + unreducedExpr.toString)

  remoteActor ! unreducedExpr.toJson

  override def receive: Receive = {
    case msg: JsValue =>
      val expr = msg.convertTo[BooleanExpression]
      println("Got message from remote: " + expr.toString)
  }
}