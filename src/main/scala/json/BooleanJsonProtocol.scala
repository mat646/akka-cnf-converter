package json

import cats.data.NonEmptyList
import cats.syntax.functor._
import cats.syntax.reducible._
import domain._
import io.circe.Json
import io.circe.generic.auto._
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.deriveDecoder
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
  val notDecoder: Decoder[Not] = deriveDecoder
  val orDecoder: Decoder[Or] = deriveDecoder
  val andDecoder: Decoder[And] = deriveDecoder
  val varDecoder: Decoder[Variable] = deriveDecoder

  implicit val exprDecoder: Decoder[BooleanExpression] =
    NonEmptyList.of(
      trueDecoder.widen[BooleanExpression],
      falseDecoder.widen[BooleanExpression],
      notDecoder.widen[BooleanExpression],
      orDecoder.widen[BooleanExpression],
      andDecoder.widen[BooleanExpression],
      varDecoder.widen[BooleanExpression]).reduceK

}
