package magnus.com.sokoban.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2

/**
 * Class representing a target for the user to reach.
 */
class Target(position : Vector2) : Actor(Texture("target1.png")) {

  init {
    setPosition(position.x * WorldConstants.CELL_SIZE, position.y * WorldConstants.CELL_SIZE)
  }
}