package one.xingyi.character4

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future

class LoggingSpec extends KataSpec with FunctionFixture with NonFunctional {

  behavior of "Default Logging Data"

  it should "do a tostring" in {
    implicitly[LogData[Int]] apply 123 shouldBe "123"
  }

  behavior of "Logging"

  val loggingFn = logging[Int, Int]("some pattern {0}, {1}") apply fn(1, Future(2))
  it should "return the delegate function" in {
    loggingFn(1) .await shouldBe 2
  }

  it should "log data" in {
    val (e, s) = valueAndLog(loggingFn(1).await)
    s.trim shouldBe "some pattern 1, 2"
  }
}
