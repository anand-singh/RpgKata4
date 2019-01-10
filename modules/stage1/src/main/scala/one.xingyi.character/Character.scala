package one.xingyi.character
import java.util.concurrent.atomic.AtomicInteger

trait LiveStatus

case object Alive extends LiveStatus
case object Dead extends LiveStatus

case class HitPoints(hp: Int) {
  def +(that: HitPoints): HitPoints = HitPoints(hp + that.hp)
  def -(that: HitPoints): HitPoints = HitPoints(hp - that.hp)
  def lessThanZero: Boolean = hp < 0
}

trait Logger {
  def info(msg: String) = println(msg)
  def error(msg: String, e: Exception) = {println(msg); e.printStackTrace}
}

case class Character(name: String, alive: LiveStatus = Alive, hitPoints: HitPoints = HitPoints(1000)) extends Logger {

  private val damageCount = new AtomicInteger()

  def damage(hitPoints: HitPoints) = try {
    if (hitPoints.lessThanZero) this else {
      val newHitPoints = this.hitPoints - hitPoints
      damageCount.incrementAndGet()
      val result = if (newHitPoints.lessThanZero)
        copy(alive = Dead, hitPoints = HitPoints(0))
      else
        copy(hitPoints = newHitPoints)
      info(s"Damaged $this for $hitPoints producing $newHitPoints")
      result
    }
  }
  catch {
    case e: Exception =>
      error(s"Unexpected error damaging $this for $hitPoints", e);
      throw e
  }
}
