package magnus.com.sokoban.game.level

import com.badlogic.gdx.Gdx
import magnus.com.sokoban.game.WorldConstants

/**
 * Class to list all available levels and keep track of current selected one.
 */
class LevelManager {

  private var currentLevel: Level?

  init {
    currentLevel = null
  }

  fun listAllLevelNames(): List<String> {
    val levelNameList = ArrayList<String>()
    val directoryHandler = Gdx.files.internal(WorldConstants.LEVELS_FILE_PATH)
    directoryHandler.list()
        .filterNot { it.isDirectory }
        .mapTo(levelNameList) { it.name() }
    return levelNameList
  }

  fun getLevel(name: String): Level? {
    return LevelParser.parseLevel(name)
  }

  fun selectLevel(level: Level) {
    currentLevel = level
  }

  fun getCurrentLevel(): Level? {
    return currentLevel
  }

  /**
   * Get the next level if there is one, otherwise {@code null}. Will not select a new level.
   */
  fun nextLevel(): Level? {
    var foundCurrentLevel = false
    for (file in Gdx.files.internal(WorldConstants.LEVELS_FILE_PATH).list()) {
      if (!file.isDirectory) {
        val levelName = clipFileName(file.name())

        // If foundCurrentLevel is true then this is the next level!
        if(foundCurrentLevel) {
          return LevelParser.parseLevel(levelName)
        }

        if (clipFileName(file.name()) == currentLevel?.name) {
          foundCurrentLevel = true
        }
      }
    }

    // No next level was found!
    return null
  }

  private fun clipFileName(filename: String): String {
    return filename.substringAfter(WorldConstants.LEVELS_FILE_PATH)
  }
}