package json

import cats.syntax.functor._
import domain._
import io.circe.Json
import io.circe.generic.auto._
import io.circe.{Decoder, Encoder}
import io.circe.syntax._

trait BooleanJsonProtocol {

  implicit val encodeEvent: Encoder[BooleanExpression] = Encoder.instance {
    case v: Variable => v.asJson
    case not: Not => not.asJson
    case or: Or => or.asJson
    case and: And => and.asJson
    case True => Json.fromString("true")
    case False => Json.fromString("false")
  }

  val trueDecoder: Decoder[True.type] = Decoder[String].emap {
    case "true" => Right(True)
    case _ => Left("Invalid value: expected 'true'")
  }
  val falseDecoder: Decoder[False.type] = Decoder[String].emap {
    case "false" => Right(False)
    case _ => Left("Invalid value: expected 'false'")
  }

  implicit val exprDecoder: Decoder[BooleanExpression] =
    List[Decoder[BooleanExpression]](
      trueDecoder.widen,
      falseDecoder.widen,
      Decoder[Not].widen,
      Decoder[Or].widen,
      Decoder[And].widen,
      Decoder[Variable].widen).reduceLeft(_ or _)

}
