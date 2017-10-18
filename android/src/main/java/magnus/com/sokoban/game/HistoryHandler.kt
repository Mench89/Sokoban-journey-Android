package magnus.com.sokoban.game

import com.badlogic.gdx.math.Vector2
import magnus.com.sokoban.game.level.Level

/**
 * Class to save and restore movement history of player and moveable objects.
 */
class HistoryHandler(val level: Level) {

  val playerHistory = ArrayList<Vector2>()
  val boxesHistory = HashMap<Box, ArrayList<Vector2>>()

  init {
    for (box in level.boxes) {
      boxesHistory.put(box, ArrayList<Vector2>())
    }
  }

  /**
   * To be called after every "world tick", e.g when user has performed a movement and the world
   * have been updated.
   */
  fun saveCurrentTime() {
    playerHistory.add(level.player.getPosition())

    for (box in level.boxes) {
      boxesHistory[box]?.add(box.getPosition())
    }
  }

  /**
   * Go back in time and revert one world tick!
   */
  fun timeTravel() {
    if (!playerHistory.isEmpty()) {
      level.player.setPosition(playerHistory.removeAt(playerHistory.size - 1))
    }

    for (box in level.boxes) {
      val positionList = boxesHistory[box]
      if (positionList != null && !positionList.isEmpty()) {
        box.setPosition(positionList.removeAt(positionList.size - 1))
      }
    }
  }
}