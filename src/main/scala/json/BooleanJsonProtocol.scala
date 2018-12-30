package json

import domain._
import spray.json._

trait BooleanJsonProtocol extends DefaultJsonProtocol {

  implicit val varImpl: JsonFormat[Variable] = jsonFormat1(Variable)

  implicit val booleanExpressionFormat: JsonFormat[BooleanExpression] = new JsonFormat[BooleanExpression] {
    def write(obj: BooleanExpression): JsValue =
      obj match {
        case v: Variable => v.toJson
        case not: Not => not.toJson
        case or: Or => or.toJson
        case and: And => and.toJson
        case _: True.type => JsTrue
        case _: False.type => JsFalse
        case _ => deserializationError("Out of domain")
      }

    def read(json: JsValue): BooleanExpression =
      json match {
        case JsTrue => True
        case JsFalse => False
        case _ =>
          json.asJsObject.fields.keySet.toSeq match {
            case Seq("and1", "and2") => json.convertTo[And]
            case Seq("or1", "or2") => json.convertTo[Or]
            case Seq("not") => json.convertTo[Not]
            case Seq("symbol") => json.convertTo[Variable]
            case _ => serializationError("Out of domain")
          }
      }
  }

  implicit val notImpl: JsonFormat[Not] = lazyFormat(jsonFormat(Not, "not"))
  implicit val orImpl: JsonFormat[Or] = lazyFormat(jsonFormat(Or, "or1", "or2"))
  implicit val andImpl: JsonFormat[And] = lazyFormat(jsonFormat(And, "and1", "and2"))
}
