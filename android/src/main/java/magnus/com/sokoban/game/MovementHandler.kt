package magnus.com.sokoban.game

import android.util.Log
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import magnus.com.sokoban.game.level.Level

class MovementHandler(val level: Level, val listener: MovementListener) : InputHandler.UserInteractionListener {

  var isPlayerPanning = false
  var previousPannedPosition: Rectangle?

  override fun onUserPanStopped(pannedPoint: Vector3) {
    isPlayerPanning = false
    previousPannedPosition = null
  }

  override fun onUserTouchedDown(tappedPoint: Vector3) {
    if (userClickedOnPlayer(tappedPoint)) {
      isPlayerPanning = true
    }
  }

  override fun onUserPan(pannedPoint: Vector3) {
    // Only pan the player if user moved a small distance away from player.
    var panningThreshold = WorldConstants.CELL_SIZE * 0.70
    if (previousPannedPosition?.contains(Vector2(pannedPoint.x, pannedPoint.y)) == true) {
      Log.d("Sokoban Game", "Trying to go back go previous square, increase panning threshold")
      panningThreshold = WorldConstants.CELL_SIZE * 0.90
    }
    if (isPlayerPanning && level.player.getCenterPosition().dst(pannedPoint.x, pannedPoint.y) > panningThreshold) {
      onUserTapped(pannedPoint)
    }
  }

  val gestureDetector: GestureDetector?

  interface MovementListener {
    fun onBeforePlayerMoved()
    fun onAfterPlayerMoved()
  }

  init {
    val camera = OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    camera.setToOrtho(false, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    gestureDetector = GestureDetector(InputHandler(camera, this))
    previousPannedPosition = null
  }

  override fun onUserTapped(tappedPoint: Vector3) {
    if (userClickedOnPlayer(tappedPoint)) {
      return
    }

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

    val soonPreviousPanningPosition = Rectangle(level.player.shape)
    val movedPlayerShape = Rectangle(level.player.shape)
    movedPlayerShape.setPosition(level.player.getX() + newPosXDiff, level.player.getY() + newPosyDiff)
    if (CollisionHelper.isCollidingWithAny(movedPlayerShape, level.walls.wallShapes)) {
      Log.d("Sokoban game", "Player went into a wall!")
      // Player went into a wall! Don't move player
    } else {
      if (!checkForBoxCollision(movedPlayerShape, newPosXDiff, newPosyDiff)) {
        if (isPlayerPanning) {
          previousPannedPosition = soonPreviousPanningPosition
        }
        level.player.setPosition(movedPlayerShape.getPosition(Vector2()))
        listener.onAfterPlayerMoved()
      }
    }
  }

  /**
   * Check if player moved any box and if the box itself didn't collied with anything.
   * @return true if box collided and couldn't be moved.
   */
  private fun checkForBoxCollision(newPlayerShape: Rectangle, newPosXDiff: Float, newPosYDiff: Float): Boolean {
    for (box in level.boxes) {
      if (CollisionHelper.isColliding(newPlayerShape, box.shape)) {
        val movedBox = Box(Vector2())
        movedBox.setPosition(box.getX() + newPosXDiff, box.getY() + newPosYDiff)
        val boxShapes = ArrayList<Rectangle>()
        level.boxes.mapTo(boxShapes) { it.shape }
        if (CollisionHelper.isCollidingWithAny(movedBox.shape, level.walls.wallShapes) || CollisionHelper.numberOfCollisions(movedBox.shape, boxShapes) > 0) {
          // Box collided with wall, don't move box.
          return true
        } else {
          listener.onBeforePlayerMoved()
          box.setPosition(movedBox.getPosition())
          return false
        }
      }
    }
    listener.onBeforePlayerMoved()
    return false
  }

  private fun userClickedOnPlayer(tappedPoint: Vector3): Boolean {
    return level.player.shape.contains(Vector2(tappedPoint.x, tappedPoint.y))
  }
}