package magnus.com.sokoban.game

import com.badlogic.gdx.graphics.Texture

/**
 * Class representing the player.
 */
class Player : Actor(Texture("player.png")) {
  init {
    setPosition(96F, 96F)
  }
}