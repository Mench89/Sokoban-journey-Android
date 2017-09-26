package magnus.com.sokoban.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class World : Drawable {
  val background = Texture("background.png")

  init {
    background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat)
  }

  override fun draw(batch: SpriteBatch) {
    batch.draw(background, 0F, 0F, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
  }
}