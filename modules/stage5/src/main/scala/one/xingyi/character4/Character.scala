package one.xingyi.character4

import java.util.concurrent.atomic.AtomicInteger

trait LiveStatus

case object Alive extends LiveStatus
case object Dead extends LiveStatus


case class Faction(name: String)

case class Level(l: Int) {
  def isFiveOrMoreHigher(other: Level) = l - 5 >= other.l
}



trait FunctionTransformer[From, To] extends ((From => To) => (From => To))

trait NonFunctional {
  implicit class FunctionTransformerOps[From, To](fn1: FunctionTransformer[From, To]) {
    def |+|(fn2: FunctionTransformer[From, To]): FunctionTransformer[From, To] = f => fn2(fn1(f))
  }

  def logging[From, To](pattern: String): FunctionTransformer[From, To] = fn => new Logging(pattern, fn)
  def metrics[From, To](int: AtomicInteger): FunctionTransformer[From, To] = fn => new Metrics[From, To](int, fn)
  def errorHandling[From, To]: FunctionTransformer[From, To] = fn => new ErrorHandler[From, To](fn)
}

//not looking at non functional right now.

case class Character(name: String, faction: Faction, range: Range = Melee, level: Level = Level(1), alive: LiveStatus = Alive, hitPoints: HitPoints = HitPoints(1000)) {
  def damage(attacker: Character, distance: Meters, hitPoints: HitPoints): Character =
    if ((attacker != this) && (attacker.range.canHit(distance)) && attacker.faction != faction) {
      if (hitPoints.lessThanZero) this else {
        val damage = (level, attacker.level) match {
          case (l1, l2) if l1 isFiveOrMoreHigher l2 => hitPoints + hitPoints.fiftyPercent
          case (l1, l2) if l2 isFiveOrMoreHigher l1 => hitPoints - hitPoints.fiftyPercent
          case _ => hitPoints
        }
        val newHitPoints = this.hitPoints - damage
        if (newHitPoints.lessThanZero)
          copy(alive = Dead, hitPoints = HitPoints(0))
        else
          copy(hitPoints = newHitPoints)
      }
    } else this
}

object Character extends NonFunctional {
  val damageCount = new AtomicInteger()
  def nonFunctional[From, To] = logging[From, To]("Damaged {1}") |+| metrics(damageCount)


}

