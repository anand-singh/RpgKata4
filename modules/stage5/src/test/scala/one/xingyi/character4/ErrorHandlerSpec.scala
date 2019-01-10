package one.xingyi.character4

import java.io.{ByteArrayOutputStream, PrintStream}

class ErrorHandlerSpec extends KataSpec with FunctionFixture {

  behavior of "Error Handler"

  it should "return the delegate if no error" in {
    new ErrorHandler(fn(1, 2))(1) shouldBe 2
  }

  it should "print stuff if an error happens and throw the error" in {

    val error = new RuntimeException("somemessage")
    val (e, s) = valueAndLog(intercept[RuntimeException](new ErrorHandler(fn(1, throw error))(1)))

    e shouldBe  error
    s should include("somemessage")
  }
}
