package domain

sealed trait BooleanExpression

case object True extends BooleanExpression

case object False extends BooleanExpression

case class Variable(symbol: String) extends BooleanExpression {
  override def toString: String = symbol
}

case class Not(not: BooleanExpression) extends BooleanExpression {
  override def toString: String =  s"~ (${not.toString}) "
}

case class Or(or1: BooleanExpression, or2: BooleanExpression) extends BooleanExpression {
  override def toString: String = s" (${or1.toString}) v (${or2.toString}) "
}

case class And(and1: BooleanExpression, and2: BooleanExpression) extends BooleanExpression {
  override def toString: String = s" (${and1.toString}) ^ (${and2.toString}) "
}


object BooleanExpression {
  def convertToCNF(expr: BooleanExpression): BooleanExpression = expr match {
    case True => True
    case False => False
    case Variable(symbol) => Variable(symbol)
    case Not(False) => True
    case Not(True) => False
    case Not(Not(exp)) => convertToCNF(exp)
    case Not(And(and1, and2)) => convertToCNF(Or(Not(and1), Not(and2)))
    case Not(Or(or1, or2)) => convertToCNF(And(Not(or1), Not(or2)))
    case Not(Variable(symbol)) => Not(Variable(symbol))
    case And(and1, and2) => And(convertToCNF(and1), convertToCNF(and2))
    case Or(or1, or2) =>
      val convertedOr1 = convertToCNF(or1)
      val convertedOr2 = convertToCNF(or2)
      (convertedOr1, convertedOr2) match {
        case (And(and11, and12), And(and21, and22)) =>
          And(And(Or(and11, and21), Or(and11, and22)), And(Or(and12, and21), Or(and12, and22)))
        case (And(and1, and2), exp @ (_: Variable | _: Not)) => And(Or(and1, exp), Or(and2, exp))
        case (exp @ (_: Variable | _: Not), And(and1, and2)) => And(Or(and1, exp), Or(and2, exp))
        case _ => Or(convertedOr1, convertedOr2)
      }
  }
}
