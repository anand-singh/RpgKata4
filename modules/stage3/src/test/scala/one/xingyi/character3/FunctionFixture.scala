package one.xingyi.character3

import java.util.concurrent.atomic.AtomicReference

import org.scalatest.Matchers

trait FunctionFixture extends Matchers {
  def fn[X, Y](expected: X, y: => Y) = { x: X => x shouldBe expected; y }
  def fn2[X, Y, Z](expectedX: X, expectedY: Y, z: => Z) = { (x: X, y: Y) => x shouldBe expectedX; y shouldBe expectedY; z }
  def fn2Curried[X, Y, Z](expectedX: X, expectedY: Y, z: => Z) = { x: X => y: Y => x shouldBe expectedX; y shouldBe expectedY; println(s"fn2 $expectedX, $expectedY, $z"); z }
  def sideeffect[X](atomicReference: AtomicReference[X]): X => Unit = atomicReference.set _

}