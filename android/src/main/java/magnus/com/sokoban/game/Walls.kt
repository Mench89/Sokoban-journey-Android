package magnus.com.sokoban.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

/**
 * Class representing multiple tiles of walls. This is to reduce the number of duplicates of wall textures.
 */
class Walls(positions : List<Vector2>) : Drawable {

  val texture = Texture("wall1.png")
  val wallShapes = ArrayList<Rectangle>()

  init {
    // Convert position to world coordinates
    positions.mapTo(wallShapes) { createWallShape(it.x * WorldConstants.CELL_SIZE, it.y * WorldConstants.CELL_SIZE) }
  }

  // Only for testing.
  /*
  var wallShapes = arrayOf<Rectangle>(createWallShape(0F, 0F),
      createWallShape(0F, WorldConstants.CELL_SIZE),
      createWallShape(0F, WorldConstants.CELL_SIZE * 2),
      createWallShape(0F, WorldConstants.CELL_SIZE * 3),
      createWallShape(0F, WorldConstants.CELL_SIZE * 4),
      createWallShape(WorldConstants.CELL_SIZE * 2, 0F),
      createWallShape(WorldConstants.CELL_SIZE * 2, WorldConstants.CELL_SIZE),
      createWallShape(WorldConstants.CELL_SIZE * 2, WorldConstants.CELL_SIZE * 2),
      createWallShape(WorldConstants.CELL_SIZE * 2, WorldConstants.CELL_SIZE * 3),
      createWallShape(WorldConstants.CELL_SIZE * 2, WorldConstants.CELL_SIZE * 4))
      */


  override fun draw(batch: SpriteBatch) {
    for (wallPosition in wallShapes) {
      batch.draw(texture, wallPosition.x, wallPosition.y, WorldConstants.CELL_SIZE, WorldConstants.CELL_SIZE)
    }
  }

  private fun createWallShape(x: Float, y: Float): Rectangle {
    return Rectangle(x, y, WorldConstants.CELL_SIZE, WorldConstants.CELL_SIZE)
  }
}