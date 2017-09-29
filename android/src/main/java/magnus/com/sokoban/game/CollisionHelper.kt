package magnus.com.sokoban.game

import com.badlogic.gdx.math.Rectangle

/**
 * Helper for deciding collisions between different objects.
 */
class CollisionHelper private constructor() {

  companion object {

    /**
     * Is the two objects colliding?
     */
    fun isColliding(anObject: Rectangle, other: Rectangle): Boolean {
      return anObject.overlaps(other)
    }

    /**
     * Is the object colliding with any of the other objects?
     */
    fun isCollidingWithAny(anObject: Rectangle, others: Array<Rectangle>): Boolean {
      return others.any { it.overlaps(anObject) }
    }
  }
}