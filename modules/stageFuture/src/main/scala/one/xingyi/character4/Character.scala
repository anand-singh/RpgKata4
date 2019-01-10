package one.xingyi.character4

import java.util.concurrent.atomic.AtomicInteger

import FunctionLanguage._
import one.xingyi.character4.Character.metrics

import scala.concurrent.{ExecutionContext, Future}
trait LiveStatus

case object Alive extends LiveStatus
case object Dead extends LiveStatus

case class HitPoints(hp: Int) {
  def +(that: HitPoints): HitPoints = HitPoints(hp + that.hp)
  def -(that: HitPoints): HitPoints = HitPoints(hp - that.hp)
  def lessThanZero: Boolean = hp < 0
}

case class Character(name: String, alive: LiveStatus = Alive, hitPoints: HitPoints = HitPoints(1000)) {
  def damage(hitPoints: HitPoints)(implicit ec: ExecutionContext)  = Character.damage(hitPoints) apply this
}



object Character extends NonFunctional {
  val damageCount = new AtomicInteger()

  def nonFunctional[From, To](implicit ec: ExecutionContext) = metrics[From,To](damageCount) |+| logging[From, To]("Damaged {1}")  |+| errorHandling

  def hitpointL = Lens[Character, HitPoints](_.hitPoints, h => c => c.copy(hitPoints = h))
  def liveStatusL = Lens[Character, LiveStatus](_.alive, as => c => c.copy(alive = as))

  def kill = hitpointL.set(HitPoints(0)) andThen liveStatusL.set(Dead)
  val killIfNegativeHitPoints = hitpointL.has(_.lessThanZero) ifTrue kill
  def reduceHitpoints(hitPoints: HitPoints): Character => Character = hitpointL.transform(_ - hitPoints)

  def damage(hitPoints: HitPoints)(implicit ec: ExecutionContext) : Character => Future[Character] =
    if (hitPoints.lessThanZero) Future.successful else
      nonFunctional[Character, Character] apply (reduceHitpoints(hitPoints) andThen killIfNegativeHitPoints).liftFn()
}

