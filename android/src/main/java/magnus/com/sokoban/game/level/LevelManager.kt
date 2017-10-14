package magnus.com.sokoban.game.level

import com.badlogic.gdx.Gdx
import magnus.com.sokoban.game.WorldConstants

/**
 * Class to list all available levels and keep track of current selected one.
 */
class LevelManager {

  private var currentLevelName = ""

  fun listAllLevelNames(): List<String> {
    val levelNameList = ArrayList<String>()
    val directoryHandler = Gdx.files.internal(WorldConstants.LEVELS_FILE_PATH)
    directoryHandler.list()
        .filterNot { it.isDirectory }
        .mapTo(levelNameList) { it.name() }
    return levelNameList
  }

  fun selectLevel(name: String): Level {
    currentLevelName = name
    return LevelParser.parseLevel(name)
  }

  fun nextLevel(): Level? {
    var foundCurrentLevel = false
    for (file in Gdx.files.internal(WorldConstants.LEVELS_FILE_PATH).list()) {
      if (!file.isDirectory) {
        if (file.name() == currentLevelName) {
          foundCurrentLevel = true
          continue
        }
        // If foundCurrentLevel is true then this is the next level!
        if(foundCurrentLevel) {
          return selectLevel(file.name())
        }
      }
    }

    // No next level was found!
    return null
  }

  private fun clipFileName(): String {

  }
  // TODO: Go to next level.
  // TODO: Get current level.
  // TODO:
}