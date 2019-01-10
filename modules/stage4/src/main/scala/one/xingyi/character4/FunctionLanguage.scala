package one.xingyi.character4

object FunctionLanguage extends FunctionLanguage
trait FunctionLanguage {
  implicit class FnOps[From, To](fn: From => To) {
    def sideeffectAfter(block: => Unit) = { from: From => val result = fn(from); block; result }
    def sideeffectFnAfter(block: Tuple2[From, To] => Unit) = { from: From => val result = fn(from); block(from, result); result }
    def onError(block: Exception => To) = { from: From => try {fn(from)} catch {case e: Exception => block(e)} }
  }
  implicit class BooleanFunctionPimper[T](fn: T => Boolean) {
    def ifTrue(guardedFunction: T => T) = { t: T => if (fn(t)) guardedFunction(t) else t }
  }

  def identity[X]= {x: X=>x}
}
