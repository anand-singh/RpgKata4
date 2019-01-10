package one.xingyi.character3

import java.util.concurrent.atomic.AtomicInteger

class MetricsSpec extends KataSpec with FunctionFixture {

  behavior of "Metrics"

  it should "return the same value as the delegate" in {
    new Metrics(new AtomicInteger, fn(1, 2)) apply 1 shouldBe 2
  }

  it should "increment the counter each time it is called " in {
    val count = new AtomicInteger
    val metrics = new Metrics[Int, Int](count, fn(1, 2))
    metrics(1) shouldBe 2
    count.get shouldBe 1

    metrics(1) shouldBe 2
    count.get shouldBe 2

    metrics(1) shouldBe 2
    count.get shouldBe 3
  }

}
