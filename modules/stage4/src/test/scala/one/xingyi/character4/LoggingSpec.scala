package one.xingyi.character4

class LoggingSpec extends KataSpec with FunctionFixture with NonFunctional {

  behavior of "Default Logging Data"

  it should "do a tostring" in {
    implicitly[LogData[Int]] apply 123 shouldBe "123"
  }

  behavior of "Logging"

  val loggingFn = logging[Int, Int]("some pattern {0}, {1}")(fn(1, 2))
  it should "return the delegate function" in {
    loggingFn(1) shouldBe 2
  }

  it should "log data" in {
    val (e, s) = valueAndLog(loggingFn(1))
    s.trim shouldBe "some pattern 1, 2"
  }
}
