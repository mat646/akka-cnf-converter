object Warmup extends App {

  def f: PartialFunction[Int, Int] =  {
    case 0 => 1
    case x if x > 0 => f(x-1) + f(x-1)
  }

  println(f(10))

  /**
    * Big-O complexity is O(2 pow N) so exponential.
    * We can reduce it by introducing memorization of computed values.
    * So once computed e.g. f(10) write it to array at index 10.
    * Then every time recursion hits f(10) first check whether is already
    * computed, if not then compute.
    * After this improvement complexity is O(N).
    */

}
