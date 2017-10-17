package magnus.com.sokoban.game.level

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import magnus.com.sokoban.game.*
import magnus.com.sokoban.game.Target

class Level(val fileName: String, val player: Player, val world: World, val walls: Walls, val floor: Floor, val boxes: List<Box>, val targets: List<Target>)
  : Drawable {

  val name = fileName.substringBefore(".xml")

  override fun draw(batch: SpriteBatch) {
    world.draw(batch)
    floor.draw(batch)
    walls.draw(batch)
    for (target in targets) {
      target.draw(batch)
    }
    for (box in boxes) {
      box.draw(batch)
    }
    player.draw(batch)
  }
}