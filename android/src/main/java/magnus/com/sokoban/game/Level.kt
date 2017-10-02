package magnus.com.sokoban.game

import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Level(val player: Player, val world: World, val walls: Walls, val floor: Floor, val boxes: Array<Box>, val targets: Array<Target>, listener: TargetStateModel.GameStateListener)
  : Drawable{

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