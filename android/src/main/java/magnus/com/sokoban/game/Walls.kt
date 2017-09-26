package magnus.com.sokoban.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2

class Walls : Drawable {

  val texture = Texture("wall1.png")
  // TODO: Change to shape to check for collisions.
  var wallPositions = arrayOf<Vector2>(Vector2(0F, 0F), Vector2(0F, 96F))

  override fun draw(batch: SpriteBatch) {
    batch.draw(texture, wallPositions[0].x, wallPositions[0].y, 96F, 96F)
    batch.draw(texture, wallPositions[1].x, wallPositions[1].y, 96F, 96F)
  }
}