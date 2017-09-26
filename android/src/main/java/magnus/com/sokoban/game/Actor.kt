package magnus.com.sokoban.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle

open class Actor(val sprite: Texture) {
  val shape = Rectangle(0F, 0F, sprite.width.toFloat(), sprite.height.toFloat())

  fun setPosition(x: Float, y: Float) {
    shape.setPosition(x, y)
  }

  fun getX() : Float {
    return shape.x
  }

  fun getY() : Float {
    return shape.y
  }

  fun draw(batch : SpriteBatch) {
    batch.draw(sprite, shape.x, shape.y)
  }
}