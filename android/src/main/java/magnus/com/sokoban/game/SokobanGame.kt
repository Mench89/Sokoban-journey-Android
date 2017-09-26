package magnus.com.sokoban.game;

import android.util.Log
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.ExtendViewport



class SokobanGame : ApplicationAdapter(), InputProcessor {


  // we will use 32px/unit in world
  val SCALE = 32f
  val INV_SCALE = 1f / SCALE
  // this is our "target" resolution, not that the window can be any size, it is not bound to this one
  val VP_WIDTH = 1280 * INV_SCALE
  val VP_HEIGHT = 720 * INV_SCALE

  private var camera: OrthographicCamera? = null
  private var viewport: ExtendViewport? = null
  private var shapes: ShapeRenderer? = null

  lateinit var batch: SpriteBatch
  lateinit var img: Texture
  lateinit var player: Player

  override fun create() {
    batch = SpriteBatch()
    img = Texture("player.png")
    player = Player()

    camera = OrthographicCamera()
    // pick a viewport that suits your thing, ExtendViewport is a good start
    viewport = ExtendViewport(VP_WIDTH, VP_HEIGHT, camera)
    // ShapeRenderer so we can see our touch point
    shapes = ShapeRenderer()
    Gdx.input.setInputProcessor(this)
  }

  override fun render() {
    Gdx.gl.glClearColor(1F, 0F, 0F, 1F)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    batch.begin()
    batch.draw(img, 0F, 0F)
    player.draw(batch)
    batch.end()
  }

  override fun dispose() {
    batch.dispose()
    img.dispose()
  }

  override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
    return false
  }

  override fun keyTyped(character: Char): Boolean {
    return false
  }

  override fun scrolled(amount: Int): Boolean {
    return false
  }

  override fun keyUp(keycode: Int): Boolean {
    return false
  }

  override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
    return false
  }

  override fun keyDown(keycode: Int): Boolean {
    return false
  }

  override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
    return false
  }

  override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
    var newPosXDiff = 0
    var newPosyDiff = 0
    val screenXRelativePlayerX = screenX - player.getX().toInt()
    val screenXRelativePlayerY = screenY - player.getY().toInt()

    if (Math.max(Math.abs(screenX), Math.abs(screenY)) == screenX) {
      if(screenXRelativePlayerX > 0) {
        newPosXDiff = 50
        Log.d("Sokoban game", "Going right")
      } else {
        newPosXDiff = -50
        Log.d("Sokoban game", "Going left")
      }
    } else {
      if(screenXRelativePlayerY > 0) {
        newPosyDiff = 50
        Log.d("Sokoban game", "Going up")
      } else {
        newPosyDiff = -50
        Log.d("Sokoban game", "Going down")
      }
    }
      player.setPosition(player.getX() + newPosXDiff, player.getY() + newPosyDiff)
    return true
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}
