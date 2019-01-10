package one.xingyi.character4
import scala.concurrent.{ExecutionContext, Future}

object FunctionLanguage extends FunctionLanguage
trait FunctionLanguage {
  implicit class FunctionOps[From, To](fn: From => To) {
    def liftFn() = { from: From => Future.successful(fn(from)) }

  }
  implicit class FnFutureOps[From, To](fn: From => Future[To])(implicit ec: ExecutionContext) {
    def sideeffect(block: => Unit) = { from: From => val result = fn(from); block; result }
    def sideeffectFn(block: Tuple2[From, To] => Unit) = { from: From => fn(from).map { r => block(from, r); r } }
    def onError(block: Exception => To): From => Future[To] = { from: From => try {fn(from)} catch {case e: Exception => Future.successful(block(e))} }
    def thenDo[T](fn2: To => Future[T]) = { from: From => fn(from).flatMap(fn2) }
  }

  implicit class BooleanFunctionPimper[T](fn: T => Boolean) {
    def ifTrue(guardedFunction: T => T) = { t: T => if (fn(t)) guardedFunction(t) else t }

    def identity[X] = { x: X => x }
  }
}