package json

import cats.data.NonEmptyList
import cats.syntax.functor._
import domain._
import io.circe.Json
import io.circe.generic.auto._
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.deriveDecoder
import io.circe.syntax._
import cats.syntax.reducible._

trait BooleanJsonProtocol {

  implicit val encodeEvent: Encoder[BooleanExpression] = Encoder.instance {
    case v: Variable => v.asJson
    case not: Not => not.asJson
    case or: Or => or.asJson
    case and: And => and.asJson
    case True => Json.fromString("true")
    case False => Json.fromString("false")
  }

  val bDecoder: Decoder[True.type] = Decoder[String].emap {
    case "true" => Right(True)
    case _ => Left("Invalid value: expected 'true'")
  }
  val gDecoder: Decoder[False.type] = Decoder[String].emap {
    case "false" => Right(False)
    case _ => Left("Invalid value: expected 'false'")
  }
  val cDecoder: Decoder[Not] = deriveDecoder
  val dDecoder: Decoder[Or] = deriveDecoder
  val eDecoder: Decoder[And] = deriveDecoder
  val fDecoder: Decoder[Variable] = deriveDecoder

  implicit val aDecoder: Decoder[BooleanExpression] =
    NonEmptyList.of(
      cDecoder.widen[BooleanExpression],
      fDecoder.widen[BooleanExpression],
      eDecoder.widen[BooleanExpression],
      dDecoder.widen[BooleanExpression],
      gDecoder.widen[BooleanExpression],
      bDecoder.widen[BooleanExpression]).reduceK

}