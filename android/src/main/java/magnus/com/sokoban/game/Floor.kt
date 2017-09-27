package magnus.com.sokoban.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2

class Floor : Drawable {

  val texture = Texture("floor1.png")

  // Don't need shape for wall due to no collision calculation needed.
  val positions = arrayOf<Vector2>(Vector2(WorldConstants.CELL_SIZE, 0F),
      Vector2(WorldConstants.CELL_SIZE, WorldConstants.CELL_SIZE),
      Vector2(WorldConstants.CELL_SIZE, WorldConstants.CELL_SIZE * 1),
      Vector2(WorldConstants.CELL_SIZE, WorldConstants.CELL_SIZE * 2),
      Vector2(WorldConstants.CELL_SIZE, WorldConstants.CELL_SIZE * 3))

  override fun draw(batch: SpriteBatch) {
    for (position in positions) {
      batch.draw(texture, position.x, position.y, WorldConstants.CELL_SIZE, WorldConstants.CELL_SIZE)
    }
  }
}