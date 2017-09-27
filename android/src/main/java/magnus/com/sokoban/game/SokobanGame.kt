package magnus.com.sokoban.game;

import android.util.Log
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

class SokobanGame : ApplicationAdapter(), GestureDetector.GestureListener {
  override fun touchDown(x: Float, y: Float, pointer: Int, button: Int): Boolean {
    return false
  }

  override fun fling(velocityX: Float, velocityY: Float, button: Int): Boolean {
    return false
  }

  override fun zoom(initialDistance: Float, distance: Float): Boolean {
    return false
  }

  override fun pan(x: Float, y: Float, deltaX: Float, deltaY: Float): Boolean {
    return false
  }

  override fun pinchStop() {
  }

  override fun panStop(x: Float, y: Float, pointer: Int, button: Int): Boolean {
    return false
  }

  override fun longPress(x: Float, y: Float): Boolean {
    return false
  }

  override fun pinch(initialPointer1: Vector2?, initialPointer2: Vector2?, pointer1: Vector2?, pointer2: Vector2?): Boolean {
    return false
  }

  override fun tap(x: Float, y: Float, count: Int, button: Int): Boolean {
    camera?.unproject(touchPoint.set(x, y, 0F))

    Log.d("Sokoban game", "Touch x: $x y: $y")
    var newPosXDiff = 0
    var newPosyDiff = 0
    val screenXRelativePlayerX = touchPoint.x - (player.getX().toInt() + (player.shape.width / 2))
    val screenXRelativePlayerY = touchPoint.y - (player.getY().toInt() + (player.shape.height / 2))

    if (Math.abs(screenXRelativePlayerX) > Math.abs(screenXRelativePlayerY)) {
      if(screenXRelativePlayerX > 0) {
        newPosXDiff = 96
        Log.d("Sokoban game", "Going right")
      } else {
        newPosXDiff = -96
        Log.d("Sokoban game", "Going left")
      }
    } else {
      if(screenXRelativePlayerY > 0) {
        newPosyDiff = 96
        Log.d("Sokoban game", "Going up")
      } else {
        newPosyDiff = -96
        Log.d("Sokoban game", "Going down")
      }
    }
    // TODO: Validate if positoin is valid to move to.
    player.setPosition(player.getX() + newPosXDiff, player.getY() + newPosyDiff)
    if(walls.checkForCollision(player.shape)) {
      Log.d("Sokoban game", "Player went into a wall!")
      // Player went into a wall! Revert player position.
      player.setPosition(player.getX() - newPosXDiff, player.getY() - newPosyDiff)
    } else {
      checkForBoxCollision(newPosXDiff, newPosyDiff)
    }


    return true
  }

  private fun checkForBoxCollision(newPosXDiff : Int, newPosYDiff : Int) {
    // TODO: Handle new player pos if code above is changed.
    if(player.shape.overlaps(box.shape)) {
      box.setPosition(box.getX() + newPosXDiff, box.getY() + newPosYDiff)

      if(walls.checkForCollision(box.shape)) {
        // Revert player and box positions.
        player.setPosition(player.getX() - newPosXDiff, player.getY() - newPosYDiff)
        box.setPosition(box.getX() - newPosXDiff, box.getY() - newPosYDiff)
        return
      } else {


        return
      }
    }
  }

  private var camera: OrthographicCamera? = null

  lateinit var batch: SpriteBatch
  lateinit var player: Player
  lateinit var world: World
  lateinit var walls: Walls
  lateinit var floor: Floor
  lateinit var box: Box
  var touchPoint = Vector3()

  override fun create() {
    batch = SpriteBatch()
    player = Player()
    world = World()
    walls = Walls()
    floor = Floor()
    box = Box()
    box.setPosition(WorldConstants.CELL_SIZE, WorldConstants.CELL_SIZE * 4)

    camera = OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    camera?.setToOrtho(false, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())

    Gdx.input.inputProcessor = GestureDetector(this)
  }

  override fun render() {
    batch.begin()
    world.draw(batch)
    floor.draw(batch)
    walls.draw(batch)
    box.draw(batch)
    player.draw(batch)
    batch.end()
  }

  override fun dispose() {
    batch.dispose()
  }
}
