package magnus.com.sokoban.game

import android.util.Log
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

/**
 * Abstracts the input handling.
 */
class InputHandler(val camera : OrthographicCamera, val listener : UserInteractionListener) : GestureDetector.GestureListener {

  /**
   * Interface to report touches and other input performed by the user.
   */
  interface UserInteractionListener {

    /**
     * Called when user tapped with the point tapped.
     */
    fun onUserTapped(tappedPoint : Vector3)

    /**
     * Called when user long pressed with the point tapped.
     */
    fun onUserTouchedDown(tappedPoint : Vector3)

    /**
     * Called when user panned to the point.
     */
    fun onUserPan(pannedPoint : Vector3)

    /**
     * Called when user panned to the point.
     */
    fun onUserPanStopped(pannedPoint : Vector3)
  }

  val touchPoint = Vector3()

  override fun touchDown(x: Float, y: Float, pointer: Int, button: Int): Boolean {
    camera.unproject(touchPoint.set(x, y, 0F))

    Log.d("Sokoban game", "Touch down x: $x y: $y")
    listener.onUserTouchedDown(touchPoint)
    return false
  }

  override fun fling(velocityX: Float, velocityY: Float, button: Int): Boolean {
    return false
  }

  override fun zoom(initialDistance: Float, distance: Float): Boolean {
    return false
  }

  override fun pan(x: Float, y: Float, deltaX: Float, deltaY: Float): Boolean {
    camera.unproject(touchPoint.set(x, y, 0F))

    Log.d("Sokoban game", "Pan x: $x y: $y")
    listener.onUserPan(touchPoint)
    return false
  }

  override fun pinchStop() {
  }

  override fun panStop(x: Float, y: Float, pointer: Int, button: Int): Boolean {
    camera.unproject(touchPoint.set(x, y, 0F))

    Log.d("Sokoban game", "Pan stopped x: $x y: $y")
    listener.onUserPanStopped(touchPoint)
    return false
  }

  override fun longPress(x: Float, y: Float): Boolean {
    return false
  }

  override fun pinch(initialPointer1: Vector2?, initialPointer2: Vector2?, pointer1: Vector2?, pointer2: Vector2?): Boolean {
    return false
  }

  override fun tap(x: Float, y: Float, count: Int, button: Int): Boolean {
    camera.unproject(touchPoint.set(x, y, 0F))

    Log.d("Sokoban game", "Touch x: $x y: $y")
    listener.onUserTapped(touchPoint)
    return false
  }
}