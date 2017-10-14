package magnus.com.sokoban.game.level

import android.util.Xml
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.XmlReader
import magnus.com.sokoban.game.*
import magnus.com.sokoban.game.Target

class LevelParser private constructor() {
  companion object {
    fun parseLevel(levelName: String): Level? {

      try {
        val parser = Xml.newPullParser()
        val fileHandler = Gdx.files.internal(WorldConstants.LEVELS_FILE_PATH + levelName)
        parser.setInput(fileHandler.reader())
        val red = XmlReader().parse(fileHandler)
        val environmentXml = red.getChildByNameRecursive("environment")

        // Floor parsing
        val floorsXml = environmentXml.getChildByNameRecursive("floors").getChildrenByName("floor")
        val floor = Floor(parsePositions(floorsXml))

        // Wall parsing
        val wallsXml = environmentXml.getChildByNameRecursive("walls").getChildrenByName("wall")
        val walls = Walls(parsePositions(wallsXml))

        // Box parsing
        val boxesXml = environmentXml.getChildByNameRecursive("boxes").getChildrenByName("box")
        val boxes = ArrayList<Box>()
        parsePositions(boxesXml).mapTo(boxes) { Box(it) }

        // Target parsing
        val targetsXml = environmentXml.getChildByNameRecursive("targets").getChildrenByName("target")
        val targets = ArrayList<Target>()
        parsePositions(targetsXml).mapTo(targets) { Target(it) }

        // Player parsing
        val playerXml = environmentXml.getChildByNameRecursive("player")
        val player = Player(Vector2(playerXml.getFloat("x"), playerXml.getFloat("y")))

        return Level(levelName, player, World(), walls, floor, boxes, targets)
      } catch (e: Exception) {
        // Couldn't parse level.
        return null
      }
    }

    private fun parsePositions(positionsXml: Array<XmlReader.Element>): List<Vector2> {
      val positions = ArrayList<Vector2>()
      positionsXml.mapTo(positions) { Vector2(it.getFloat("x"), it.getFloat("y")) }
      return positions
    }
  }
}