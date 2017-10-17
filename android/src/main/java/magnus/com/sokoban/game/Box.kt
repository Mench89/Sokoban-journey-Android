package magnus.com.sokoban.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2

class Box(position : Vector2) : Actor(Texture("box1.png")) {

  var isOnTarget = false

  init {
    setPosition(position.x * WorldConstants.CELL_SIZE, position.y * WorldConstants.CELL_SIZE)
  }

  fun setIsOnTarget(isOnTarget: Boolean) {
    if (this.isOnTarget == isOnTarget) {
      return
    }
    this.isOnTarget = isOnTarget
    if(isOnTarget) {
      sprite = Texture("box_complete1.png")
    } else {
      sprite = Texture("box1.png")
    }
  }
}