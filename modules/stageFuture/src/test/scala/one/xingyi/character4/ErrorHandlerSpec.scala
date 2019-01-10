package one.xingyi.character4

import java.io.{ByteArrayOutputStream, PrintStream}
import scala.language.postfixOps
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._

class ErrorHandlerSpec extends KataSpec with FunctionFixture with NonFunctional {

  behavior of "Error Handler"

  it should "return the delegate if no error" in {
    (errorHandling[Int, Int] apply fn(1, Future(2)) apply 1).await shouldBe 2
  }

  it should "print stuff if an error happens and throw the error" in {

    val error = new RuntimeException("somemessage")
    val (e, s) = valueAndLog(intercept[RuntimeException](errorHandling[Int, Int] apply (fn(1, throw error)) apply (1) await))

    e shouldBe error
    s should include("somemessage")
  }
}
