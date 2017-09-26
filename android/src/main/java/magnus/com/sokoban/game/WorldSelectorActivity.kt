package magnus.com.sokoban.game

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration


class WorldSelectorActivity : AndroidApplication() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val config = AndroidApplicationConfiguration()
    config.useAccelerometer = false
    config.useCompass = false
    initialize(SokobanGame(), config)
  }
}
