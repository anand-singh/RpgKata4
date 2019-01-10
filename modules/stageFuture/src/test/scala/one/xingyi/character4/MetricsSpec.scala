package one.xingyi.character4

import java.util.concurrent.atomic.AtomicInteger

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future
class MetricsSpec extends KataSpec with FunctionFixture with NonFunctional {

  behavior of "Metrics"

  it should "return the same value as the delegate" in {
    (metrics[Int, Int](new AtomicInteger) apply (fn(1, Future(2))) apply 1).await shouldBe 2
  }

  it should "increment the counter each time it is called " in {
    val count = new AtomicInteger
    val m = metrics[Int, Int](count) apply fn(1, Future(2))
    m(1).await shouldBe 2
    count.get shouldBe 1

    m(1).await shouldBe 2
    count.get shouldBe 2

    m(1).await shouldBe 2
    count.get shouldBe 3
  }

}
