package one.xingyi.character3

import java.io.{ByteArrayOutputStream, PrintStream}
import java.lang

import org.scalatest.{FlatSpec, Matchers}


trait KataSpec extends FlatSpec with Matchers {
  def valueAndLog[X](block: => X): (X, String) = {
    val bytes = new ByteArrayOutputStream()
    val x = Console.withOut(new PrintStream(bytes))(block)
    (x, bytes.toString)
  }


}

object CharacterFixture {

  import HitPointsFixture._

  val thrud = Character("Thrud")

  def thrudWith(hitPoints: Int) = thrud.copy(hitPoints = HitPoints(hitPoints))

  val deadThrud = thrudWith(0).copy(alive = Dead)
  val thrud50HitPoints = thrudWith(50)
  val thrud100HitPoints = thrudWith(100)
  val thrud150HitPoints = thrudWith(150)
  val thrud200HitPoints = thrudWith(200)
  val thrud900HitPoints = thrudWith(900)
  val thrud0HitPoints = thrudWith(0)
  val thrudMinus1HitPoints = thrudWith(-1)
  val thrudMinus100HitPoints = thrudWith(-100)

}

class CharacterSpec extends KataSpec {

  import CharacterFixture._
  import HitPointsFixture._

  behavior of "Character"

  it should "Start with 1000 hitpoints by default" in {
    thrud.hitPoints shouldBe HitPoints(1000)
  }

  it should "start alive by default" in {
    thrud.alive shouldBe Alive
  }


  behavior of "takeing damage"

  it should "receive damage" in {
    thrud.damage(hp100) shouldBe thrud900HitPoints
    thrud200HitPoints.damage(hp100) shouldBe thrud100HitPoints

  }
  behavior of "receiving negative damage"

  it should "ignore -ve damage" in {
    thrud200HitPoints.damage(hpMinus100) shouldBe thrud200HitPoints
  }

  behavior of "Death"

  it should "die if damage received takes it negative" in {
    thrud100HitPoints.damage(hp900) shouldBe deadThrud
  }

  behavior of "LogData for character"

  it should "have a decent string" in {
    implicitly[LogData[Character]] apply thrud shouldBe "Thrud is alive: Alive with 1000"
  }
  behavior of "Logging for character"

  it should "call the logging function" in {
    val (x, msg) = valueAndLog(thrud.damage(hp100))
    msg.trim shouldBe "Damaged Character(Thrud,Alive,HitPoints(900))"
  }

}
