package magnus.com.sokoban.game

import android.util.Log

/**
 * Holds the current state of number of target reached.
 */
class TargetStateModel(val boxes: Array<Box>, val targets: Array<Target>, val listener: GameStateListener) {

  val numberOfTotalTargets = targets.size
  var numberOfReachedTargets = 0

  /**
   * Interface to report game state changes.
   */
  interface GameStateListener {

    /**
     * Called when all targets has been reached.
     */
    fun onAllTargetsReached()
  }

  init {
    if (boxes.size != targets.size) {
      Log.w("Sokoban Game", "Number of boxes and number of targets needs to be the same," +
          "or else the level will not be completable!")
    }
  }

  /**
   * Update by checking overlaps of all boxes and targets. After update is complete, check {@link numberOfReachedTargets}.
   */
  fun update() {
    var reachedTargets = 0
    for (box in boxes) {
      targets
          .filter { CollisionHelper.isColliding(box.shape, it.shape) }
          .forEach { reachedTargets++ }
    }
    numberOfReachedTargets = reachedTargets

    if (numberOfReachedTargets == numberOfTotalTargets) {
      listener.onAllTargetsReached()
    }
  }
}