import domain._
import io.circe.CursorOp.DownField
import io.circe.DecodingFailure
import json.BooleanJsonProtocol
import org.scalatest._
import io.circe.syntax._
import io.circe.parser.decode

class BooleanExpressionSpec extends FlatSpec with Matchers with BooleanJsonProtocol {

  "Boolean expression" should "parse to json" in {

    val expr: BooleanExpression = And(Not(Variable("A")), Or(Variable("B"), True))
    val fromExpr = expr.asJson.noSpaces

    val json: String = """{"and1":{"not":{"symbol":"A"}},"and2":{"or1":{"symbol":"B"},"or2":"true"}}"""

    fromExpr shouldBe json
  }

  "Json" should "parse to boolean expression domain" in {

    val expr: BooleanExpression = And(Not(Variable("A")), Or(Variable("B"), True))

    val json: String = """{"and1":{"not":{"symbol":"A"}},"and2":{"or1":{"symbol":"B"},"or2":"true"}}"""
    val convertedJson: Either[io.circe.Error, BooleanExpression] = decode[BooleanExpression](json)

    convertedJson.right.get shouldBe expr
  }

  "Deserialization of boolean expression" should "fail" in {

    val json: String = """{"and1":{"not":{"syml":"A"}},"and2":{"or1":{"symbol":"B"},"or2":true}}"""

    decode[BooleanExpression](json).left.get shouldBe DecodingFailure("Attempt to decode value on failed cursor", List(DownField("symbol")))
  }

  "Custom boolean expression" should "be reduced to CNF form" in {

    val unreducedExpr: BooleanExpression = And(Not(Variable("A")), Or(Variable("B"), And(Variable("C"), False)))

    val reducedExpr: BooleanExpression = And(Not(Variable("A")), And(Or(Variable("C"), Variable("B")), Or(False, Variable("B"))))

    BooleanExpression.convertToCNF(unreducedExpr) shouldBe reducedExpr
  }

}
