package magnus.com.sokoban.game

import com.badlogic.gdx.math.Vector2

/**
 * Class to save and restore movement history of player and moveable objects.
 */
class HistoryHandler(val level: Level) {

  val playerHistory = ArrayList<Vector2>()
  val boxesHistory = HashMap<Box, ArrayList<Vector2>>()

  init {
    playerHistory.add(level.player.getPosition())
    for (box in level.boxes) {
      val positionList = ArrayList<Vector2>()
      positionList.add(box.getPosition())
      boxesHistory.put(box, positionList)
    }
  }

  /**
   * To be called after every "world tick", e.g when user has performed a movement and the world
   * have been updated.
   */
  fun notifyWorldUpdate() {
    playerHistory.add(level.player.getPosition())

    for (box in level.boxes) {
      boxesHistory[box]?.add(box.getPosition())
    }
  }

  /**
   * Go back in time and revert one world tick!
   */
  fun timetravel() {
    // TODO: Keep original state.
    if (!playerHistory.isEmpty()) {
      if(playerHistory.size == 1) {
        level.player.setPosition(playerHistory[0])
      } else {
      level.player.setPosition(playerHistory.removeAt(playerHistory.size -1))

      }
    }

    for (box in level.boxes) {
      val positionList = boxesHistory[box]
      if (positionList != null && !positionList.isEmpty()) {
        if (positionList.size == 1) {
          box.setPosition(positionList[0])
        } else {
        box.setPosition(positionList.removeAt(positionList.size - 1))

        }
      }
    }
  }
}