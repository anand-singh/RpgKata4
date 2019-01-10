package one.xingyi.character2

import java.util.concurrent.atomic.AtomicInteger

trait LiveStatus

case object Alive extends LiveStatus
case object Dead extends LiveStatus

case class HitPoints(hp: Int) {
  def +(that: HitPoints): HitPoints = HitPoints(hp + that.hp)
  def -(that: HitPoints): HitPoints = HitPoints(hp - that.hp)
  def lessThanZero: Boolean = hp < 0
}

case class Character(name: String, alive: LiveStatus = Alive, hitPoints: HitPoints = HitPoints(1000)) {
  def damage(hitPoints: HitPoints) = Character.damage(hitPoints)(this)
}

object Character {
  val damageCount = new AtomicInteger()
  def nonFunctional(delegate: Character => Character): (Character => Character) =
    new Logging[Character, Character]("Damaged {1}",
      new Metrics(Character.damageCount,
        new ErrorHandler(
          delegate)))

  def damage(hitPoints: HitPoints) = nonFunctional { character =>
    if (hitPoints.lessThanZero) character else {
      val newHitPoints = character.hitPoints - hitPoints
      if (newHitPoints.lessThanZero)
        character.copy(alive = Dead, hitPoints = HitPoints(0))
      else
        character.copy(hitPoints = newHitPoints)
    }
  }
}

