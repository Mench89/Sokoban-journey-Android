package magnus.com.sokoban.game

import android.util.Log

/**
 * Holds the current state of number of target reached.
 */
class GameStateModel(val boxes: List<Box>, val targets: List<Target>, val listener: GameStateListener) {

  val numberOfTotalTargets = targets.size
  var numberOfReachedTargets = 0
  var numberOfSteps = 0

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
   * Update by checking overlaps of all boxes and targets. After updateAfterPlayerMovement is complete, check {@link numberOfReachedTargets}.
   */
  fun updateAfterPlayerMovement() {
    updatekNumberOfBoxTargetCollisions()
    numberOfSteps++
  }

  fun updateAfterUndo() {
    updatekNumberOfBoxTargetCollisions()
  }

  private fun updatekNumberOfBoxTargetCollisions() {
    var reachedTargets = 0
    for (box in boxes) {
      for (target in targets) {
        if(CollisionHelper.isColliding(box.shape, target.shape)) {
          box.setIsOnTarget(true)
          reachedTargets++
          break
        } else {
          box.setIsOnTarget(false)
        }
      }
    }
    numberOfReachedTargets = reachedTargets

    if (numberOfReachedTargets == numberOfTotalTargets) {
      listener.onAllTargetsReached()
    }
  }
}