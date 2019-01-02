import domain._
import json.BooleanJsonProtocol
import org.scalatest._
import spray.json._

class BooleanExpressionSpec extends FlatSpec with Matchers with BooleanJsonProtocol {

  "Boolean expression" should "parse to json" in {

    val expr: BooleanExpression = And(Not(Variable("A")), Or(Variable("B"), True))
    val fromExpr = expr.toJson

    val json: JsValue = """{"and1":{"not":{"symbol":"A"}},"and2":{"or1":{"symbol":"B"},"or2":true}}""".parseJson

    fromExpr shouldBe json
  }

  "Json" should "parse to boolean expression domain" in {

    val expr: BooleanExpression = And(Not(Variable("A")), Or(Variable("B"), True))

    val json: JsValue = """{"and1":{"not":{"symbol":"A"}},"and2":{"or1":{"symbol":"B"},"or2":true}}""".parseJson
    val convertedJson: BooleanExpression = json.convertTo[BooleanExpression]

    convertedJson shouldBe expr
  }

  "Serialization exception" should "be thrown" in {

    val json: JsValue = """{"and1":{"not":{"syml":"A"}},"and2":{"or1":{"symbol":"B"},"or2":true}}""".parseJson

    assertThrows[SerializationException] {
      json.convertTo[BooleanExpression]
    }

  }

  "Custom boolean expression" should "be reduced to CNF form" in {

    val unreducedExpr: BooleanExpression = And(Not(Variable("A")), Or(Variable("B"), And(Variable("C"), False)))

    val reducedExpr: BooleanExpression = And(Not(Variable("A")), And(Or(Variable("C"), Variable("B")), Or(False, Variable("B"))))

    BooleanExpression.convertToCNF(unreducedExpr) shouldBe reducedExpr
  }

}
