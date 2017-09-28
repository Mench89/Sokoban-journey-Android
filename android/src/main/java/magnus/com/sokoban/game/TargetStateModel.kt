package magnus.com.sokoban.game

import android.util.Log

/**
 * Holds the current state of number of target reached.
 */
class TargetStateModel(val boxes: Array<Box>, val targets: Array<Target>) {

  val numberOfTotalTargets = targets.size
  var numberOfReachedTargets = 0

  init {
    if(boxes.size != targets.size) {
      Log.w("Sokoban Game", "Number of boxes and number of targets needs to be the same," +
          "or else the level will not be completable!")
    }
  }

  /**
   * Update by checking overlaps of all boxes and targets. After update is complete, check {@link numberOfReachedTargets}.
   */
  public fun update() {
    var reachedTargets = 0
    for (box in boxes) {
      targets
          .filter { box.shape.overlaps(it.shape) }
          .forEach { reachedTargets++ }
    }
    numberOfReachedTargets = reachedTargets;
  }
}