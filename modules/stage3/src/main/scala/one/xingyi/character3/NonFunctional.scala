package one.xingyi.character3

import java.text.MessageFormat
import java.util.concurrent.atomic.AtomicInteger

trait FunctionTransformer[From, To] extends ((From => To) => (From => To))

trait NonFunctional {
  implicit class FunctionTransformerOps[From, To](fn1: FunctionTransformer[From, To]) {
    def |+|(fn2: FunctionTransformer[From, To]): FunctionTransformer[From, To] = f => fn2(fn1(f))
  }

  def logging[From:LogData, To:LogData](pattern: String): FunctionTransformer[From, To] = fn => new Logging(pattern, fn)
  def metrics[From, To](int: AtomicInteger): FunctionTransformer[From, To] = fn => new Metrics[From, To](int, fn)
  def errorHandling[From, To]: FunctionTransformer[From, To] = fn => new ErrorHandler[From, To](fn)
}

class Metrics[From, To](store: AtomicInteger, delegate: From => To) extends (From => To) {
  override def apply(from: From) = {
    val result = delegate(from)
    store.incrementAndGet()
    result
  }
}
class ErrorHandler[From, To](delegate: From => To) extends (From => To) {
  override def apply(from: From) = try {
    delegate(from)
  } catch {
    case e: Exception => println("Doing some logging here" + e); throw e
  }
}
trait LogData[T] extends (T => String)

object LogData {
  implicit def defaultLogData[T] = new LogData[T] {
    override def apply(v1: T) = v1.toString
  }
  implicit object CharacterLogData extends LogData[Character] {
    override def apply(v1: Character) = v1.name + " is alive: " + v1.alive + " with " + v1.hitPoints.hp
  }
}
/** example pattern would be 'damage {0} => {1}' */
class Logging[From, To](pattern: String, delegate: From => To)(implicit logDataF: LogData[From], logDataT: LogData[To]) extends (From => To) {
  override def apply(from: From) = {
    val result = delegate(from)
    println(MessageFormat.format(pattern, logDataF(from), logDataT(result)))
    result
  }
}
