package one.xingyi.character4

import java.text.MessageFormat
import java.util.concurrent.atomic.AtomicInteger

import scala.concurrent.{ExecutionContext, Future}

import one.xingyi.character4.FunctionLanguage._
trait FunctionTransformer[From, To] extends ((From => Future[To]) => (From => Future[To]))

trait NonFunctional {
  type FT[From, To] = FunctionTransformer[From, To]
  implicit class FunctionTransformerOps[From, To](fn1: FT[From, To]) {
    def |+|(fn2: FunctionTransformer[From, To]): FT[From, To] = f => fn2(fn1(f))
  }

/** example pattern would be 'damage {0} => {1}' */
  def logging[From, To](pattern: String)(implicit ec: ExecutionContext) : FT[From, To] = _ sideeffectFn (LogData.message[From, To](pattern) andThen println)
  def metrics[From, To](store: AtomicInteger)(implicit ec: ExecutionContext) : FT[From, To] = _ sideeffect store.incrementAndGet()
  def errorHandling[From, To](implicit ec: ExecutionContext) : FT[From, To] = _ onError { e: Exception => println("Doing some logging here" + e); throw e }
}

trait LogData[T] extends (T => String)

object LogData {
  def apply[T](t: T)(implicit logData: LogData[T]) = logData(t)
  def message[From: LogData, To: LogData](pattern: String): Tuple2[From, To] => String = {case (from, to) => MessageFormat.format(pattern, LogData(from), LogData(to))}
  implicit def defaultLogData[T]: LogData[T] = _.toString
  implicit def CharacterLogData: LogData[Character] = v1 => v1.name + " is alive: " + v1.alive + " with " + v1.hitPoints.hp
}
