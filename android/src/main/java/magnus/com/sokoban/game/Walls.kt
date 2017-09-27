package magnus.com.sokoban.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle

class Walls : Drawable {

  val texture = Texture("wall1.png")
  var wallShapes = arrayOf<Rectangle>(createWallSHape(0F, 0F),
      createWallSHape(0F, WorldConstants.CELL_SIZE),
      createWallSHape(0F, WorldConstants.CELL_SIZE * 2),
      createWallSHape(0F, WorldConstants.CELL_SIZE * 3),
      createWallSHape(0F, WorldConstants.CELL_SIZE * 4),
      createWallSHape(WorldConstants.CELL_SIZE * 2, 0F),
      createWallSHape(WorldConstants.CELL_SIZE * 2, WorldConstants.CELL_SIZE),
      createWallSHape(WorldConstants.CELL_SIZE * 2, WorldConstants.CELL_SIZE * 2),
      createWallSHape(WorldConstants.CELL_SIZE * 2, WorldConstants.CELL_SIZE * 3),
      createWallSHape(WorldConstants.CELL_SIZE * 2, WorldConstants.CELL_SIZE * 4))

  public fun checkForCollision(otherShape: Rectangle) : Boolean {
    return wallShapes.any { it.overlaps(otherShape) }
  }

  override fun draw(batch: SpriteBatch) {
    for (wallPosition in wallShapes) {
      batch.draw(texture, wallPosition.x, wallPosition.y, WorldConstants.CELL_SIZE, WorldConstants.CELL_SIZE)
    }
  }

  private fun createWallSHape(x: Float, y: Float): Rectangle {
    return Rectangle(x, y, WorldConstants.CELL_SIZE, WorldConstants.CELL_SIZE)
  }
}