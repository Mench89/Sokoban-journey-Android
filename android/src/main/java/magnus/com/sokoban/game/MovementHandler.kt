package magnus.com.sokoban.game

import android.util.Log
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3

class MovementHandler(val level: Level, val listener: MovementListener) : InputHandler.UserInteractionListener {

  val gestureDetector: GestureDetector?

  interface MovementListener {
    fun onPlayerMoved();
  }

  init {
    val camera = OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    camera.setToOrtho(false, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    gestureDetector = GestureDetector(InputHandler(camera, this))
  }

  override fun onUserTapped(tappedPoint: Vector3) {
    var newPosXDiff = 0F
    var newPosyDiff = 0F
    val screenXRelativePlayerX = tappedPoint.x - (level.player.getX().toInt() + (level.player.shape.width / 2))
    val screenXRelativePlayerY = tappedPoint.y - (level.player.getY().toInt() + (level.player.shape.height / 2))

    if (Math.abs(screenXRelativePlayerX) > Math.abs(screenXRelativePlayerY)) {
      if (screenXRelativePlayerX > 0) {
        newPosXDiff = WorldConstants.CELL_SIZE
        Log.d("Sokoban game", "Going right")
      } else {
        newPosXDiff = -WorldConstants.CELL_SIZE
        Log.d("Sokoban game", "Going left")
      }
    } else {
      if (screenXRelativePlayerY > 0) {
        newPosyDiff = WorldConstants.CELL_SIZE
        Log.d("Sokoban game", "Going up")
      } else {
        newPosyDiff = -WorldConstants.CELL_SIZE
        Log.d("Sokoban game", "Going down")
      }
    }

    level.player.setPosition(level.player.getX() + newPosXDiff, level.player.getY() + newPosyDiff)
    if (CollisionHelper.isCollidingWithAny(level.player.shape, level.walls.wallShapes)) {
      Log.d("Sokoban game", "Player went into a wall!")
      // Player went into a wall! Revert player position.
      level.player.setPosition(level.player.getX() - newPosXDiff, level.player.getY() - newPosyDiff)
    } else {
      checkForBoxCollision(newPosXDiff, newPosyDiff)
    }

    listener.onPlayerMoved()
  }

  private fun checkForBoxCollision(newPosXDiff: Float, newPosYDiff: Float) {
    // TODO: Handle new player pos if code above is changed.
    for (box in level.boxes) {
      if (CollisionHelper.isColliding(level.player.shape, box.shape)) {
        box.setPosition(box.getX() + newPosXDiff, box.getY() + newPosYDiff)

        val boxShapes = ArrayList<Rectangle>()
        level.boxes.mapTo(boxShapes) { it.shape }
        if (CollisionHelper.isCollidingWithAny(box.shape, level.walls.wallShapes) || CollisionHelper.numberOfCollisions(box.shape, boxShapes) > 1) {
          // Revert player and box positions.
          level.player.setPosition(level.player.getX() - newPosXDiff, level.player.getY() - newPosYDiff)
          box.setPosition(box.getX() - newPosXDiff, box.getY() - newPosYDiff)
          return
        }

        return
      }
    }
  }
}