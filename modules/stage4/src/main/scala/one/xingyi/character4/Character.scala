package one.xingyi.character4

import java.util.concurrent.atomic.AtomicInteger
import FunctionLanguage._
trait LiveStatus

case object Alive extends LiveStatus
case object Dead extends LiveStatus

case class HitPoints(hp: Int) {
  def +(that: HitPoints): HitPoints = HitPoints(hp + that.hp)
  def -(that: HitPoints): HitPoints = HitPoints(hp - that.hp)
  def lessThanZero: Boolean = hp < 0
}

case class Status(hitPoints: HitPoints, alive: LiveStatus)
case class Character(name: String, status: Status = Status(HitPoints(1000), Alive)) {
  def damage(hitPoints: HitPoints) = Character.damage(hitPoints)(this)
}


object Character extends NonFunctional {
  val damageCount = new AtomicInteger()
  def nonFunctional[From, To] = logging[From, To]("Damaged {1}") |+| metrics(damageCount)

  def statusL = Lens[Character, Status](_.status, s => c => c.copy(status = s))
  def statusToHitpointL = Lens[Status, HitPoints](_.hitPoints, h => c => c.copy(hitPoints = h))
  def hitpointL = statusL andThen statusToHitpointL

  def statusToliveStatusL = Lens[Status, LiveStatus](_.alive, as => c => c.copy(alive = as))
  def liveStatusL = statusL andThen statusToliveStatusL


  def kill = hitpointL.set(HitPoints(0)) andThen liveStatusL.set(Dead)
  val killIfNegativeHitPoints = hitpointL.has(_.lessThanZero) ifTrue kill
  def reduceHitpoints(hitPoints: HitPoints) = hitpointL.transform(_ - hitPoints)

  def damage(hitPoints: HitPoints): Character => Character =
    if (hitPoints.lessThanZero) identity else
      nonFunctional[Character, Character] {reduceHitpoints(hitPoints) andThen killIfNegativeHitPoints}
}

