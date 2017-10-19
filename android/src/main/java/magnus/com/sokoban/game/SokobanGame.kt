package magnus.com.sokoban.game;

import android.util.Log
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import magnus.com.sokoban.game.level.Level
import magnus.com.sokoban.game.level.LevelManager
import java.util.*

// TODO: Map editor?
// TODO: Animations
// TODO: Add credits
// TODO: Check if large maps works, if not, camera panning is maybe needed.
// TODO: Fix missing tiles on some maps.
// TODO: Sound effects.
class SokobanGame : ApplicationAdapter(), GameStateModel.GameStateListener, MovementHandler.MovementListener {

  private val TEXT_SCALE_FACTOR = 3F

  override fun onBeforePlayerMoved() {
    historyHandler.saveCurrentTime()
  }

  override fun onAfterPlayerMoved() {
    gameStateModel.updateAfterPlayerMovement()
    Log.d("Sokoban game", "Number of reached targets: " + gameStateModel.numberOfReachedTargets)
    updateTargetText()
    updateStepsText()
  }

  override fun onAllTargetsReached() {
    undoButton.remove()
    resetButton.remove()
    stage.addActor(winnerLabel)
    removePlayerInput()

    val nextLevel = levelManager.nextLevel()
    if (nextLevel != null) {
      Log.d("Sokoban game", "WINNER WINNER TACO DINNER!")

      val nextLevelButton = TextButton("Next level", skin)
      nextLevelButton.isTransform = true
      nextLevelButton.setScale(3F, 3F)
      nextLevelButton.setColor(1F, 0F, 0F, 1F)
      nextLevelButton.setPosition(Gdx.graphics.width / 2 - (nextLevelButton.width * 3F / 2), winnerLabel.y - 150)
      nextLevelButton.addListener(object : ClickListener() {
        override fun clicked(event: InputEvent?, x: Float, y: Float) {
          if (event?.type == InputEvent.Type.touchUp) {
            initWorld(nextLevel)
            initInputHandlers()
            nextLevelButton.remove()
            super.clicked(event, x, y)
          }
        }
      })

      stage.addActor(nextLevelButton)
    } else {
      // Last level was completed!
      Log.d("Sokoban game", "WINNER WINNER LAST TACO DINNER!")
      winnerLabel.setText("GZ, ALL TACO DINNERS\n  HAVE BEEN EATEN!")
      val exitButton = TextButton("Exit game", skin)
      exitButton.isTransform = true
      exitButton.setScale(3F, 3F)
      exitButton.setColor(1F, 0F, 0F, 1F)
      exitButton.setPosition(Gdx.graphics.width / 2 - (exitButton.width * 3F / 2), winnerLabel.y - 200)
      exitButton.addListener {
        Gdx.app.exit()
        true
      }
      stage.addActor(exitButton)
    }
  }

  lateinit var batch: SpriteBatch
  lateinit var skin: Skin
  lateinit var stage: Stage
  lateinit var winnerLabel: Label
  lateinit var targetLabel: Label
  lateinit var stepsLabel: Label
  lateinit var levelLabel: Label
  lateinit var movementHandler: MovementHandler
  lateinit var gameStateModel: GameStateModel
  lateinit var historyHandler: HistoryHandler
  lateinit var levelManager: LevelManager

  // Buttons
  lateinit var resetButton: TextButton
  lateinit var undoButton: TextButton

  // Sounds
  lateinit var backgroundMusic: Music
  lateinit var undoSound: Sound

  override fun create() {
    batch = SpriteBatch()
    stage = Stage()
    skin = Skin(Gdx.files.internal("skin/uiskin.json"))

    backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/theme.mp3"))
    backgroundMusic.play()
    backgroundMusic.volume = 0.2f
    backgroundMusic.isLooping = true
    undoSound = Gdx.audio.newSound(Gdx.files.internal("sounds/doh.wav"))

    levelLabel = Label("Level", skin)
    levelLabel.setFontScale(TEXT_SCALE_FACTOR)
    levelLabel.setAlignment(Align.right)

    targetLabel = Label("Boxes", skin)
    targetLabel.setFontScale(TEXT_SCALE_FACTOR)
    targetLabel.setAlignment(Align.right)

    stepsLabel = Label("Moves", skin)
    stepsLabel.setFontScale(TEXT_SCALE_FACTOR)
    stepsLabel.setAlignment(Align.right)

    winnerLabel = Label("WINNER WINNER TACO DINNER!", skin)
    winnerLabel.setFontScale(4F)
    winnerLabel.setPosition(Gdx.graphics.width / 2 - (winnerLabel.width * 2F), Gdx.graphics.height / 2 + (winnerLabel.height * 4F / 2))

    resetButton = TextButton("Reset", skin)
    resetButton.isTransform = true
    resetButton.setScale(TEXT_SCALE_FACTOR, TEXT_SCALE_FACTOR)
    resetButton.setColor(1F, 0F, 0F, 1F)
    resetButton.setPosition(Gdx.graphics.width.toFloat() - resetButton.width * TEXT_SCALE_FACTOR, 0F)
    resetButton.addListener(object : ClickListener() {
      override fun clicked(event: InputEvent?, x: Float, y: Float) {
        if (event?.type == InputEvent.Type.touchUp) {
          Log.d("Sokoban game", "Pressing reset!")
          levelManager.resetCurrentLevel()
          initWorld(levelManager.getCurrentLevel()!!)
          initInputHandlers()
        }
        super.clicked(event, x, y)
      }
    })

    undoButton = TextButton("Undo", skin)
    undoButton.isTransform = true
    undoButton.setScale(3F, 3F)
    undoButton.setColor(0F, 1F, 0F, 1F)
    undoButton.setPosition(Gdx.graphics.width.toFloat() - undoButton.width * 3F, resetButton.height * 4)
    undoButton.addListener(object : ClickListener() {

      override fun clicked(event: InputEvent?, x: Float, y: Float) {
        if (event?.type == InputEvent.Type.touchUp) {
          Log.d("Sokoban game", "Pressing undo!")
          undoSound.play()
          historyHandler.timeTravel()
          gameStateModel.updateAfterUndo()
          updateTargetText()
          updateStepsText()
        }
        super.clicked(event, x, y)
      }
    })

    levelManager = LevelManager()
    initWorld(levelManager.getLevel("1-01.xml")!!)
    initInputHandlers()
  }

  override fun render() {
    batch.begin()
    levelManager.getCurrentLevel()?.draw(batch)
    batch.end()
    stage.draw()
  }

  override fun dispose() {
    batch.dispose()
    stage.dispose()
    backgroundMusic.dispose()
    undoSound.dispose()
  }

  private fun initWorld(level: Level) {
    val box = Box(Vector2(1F, 4F))
    val target = Target(Vector2(1F, 8F))
    val wallPositions = ArrayList<Vector2>()
    wallPositions.add(Vector2(0F, 0F))
    wallPositions.add(Vector2(0F, 1F))
    wallPositions.add(Vector2(0F, 2F))
    wallPositions.add(Vector2(0F, 3F))
    wallPositions.add(Vector2(0F, 4F))
    wallPositions.add(Vector2(2F, 0F))
    wallPositions.add(Vector2(2F, 1F))
    wallPositions.add(Vector2(2F, 2F))
    wallPositions.add(Vector2(2F, 3F))
    wallPositions.add(Vector2(2F, 4F))
    val floorPositions = ArrayList<Vector2>()
    floorPositions.add(Vector2(1F, 0F))
    floorPositions.add(Vector2(1F, 1F))
    floorPositions.add(Vector2(1F, 2F))
    floorPositions.add(Vector2(1F, 3F))
    //level = Level(Player(Vector2(1F, 1F)), World(), Walls(wallPositions), Floor(floorPositions), Collections.singletonList(box), Collections.singletonList(target))

    levelManager.selectLevel(level)
    gameStateModel = GameStateModel(level.boxes, level.targets, this)
    historyHandler = HistoryHandler(level)

    initButtonsAndLabels()
  }

  private fun initInputHandlers() {
    movementHandler = MovementHandler(levelManager.getCurrentLevel()!!, this)
    val inputMultiplexer = InputMultiplexer()
    inputMultiplexer.addProcessor(stage)
    inputMultiplexer.addProcessor(movementHandler.gestureDetector)
    Gdx.input.inputProcessor = inputMultiplexer
  }

  private fun removePlayerInput() {
    val inputMultiplexer = Gdx.input.inputProcessor as InputMultiplexer
    inputMultiplexer.removeProcessor(movementHandler.gestureDetector)
    Gdx.input.inputProcessor = inputMultiplexer

  }

  private fun updateTargetText() {
    targetLabel.setText("Boxes\n"
        + gameStateModel.numberOfReachedTargets + "/" + gameStateModel.numberOfTotalTargets)
  }


  private fun updateStepsText() {
    stepsLabel.setText("Moves\n" +
        gameStateModel.numberOfSteps)
  }

  private fun updateLevelText() {
    levelLabel.setText("Level\n" +
        levelManager.getCurrentLevel()?.name)
  }

  private fun initButtonsAndLabels() {
    resetButton.remove()
    undoButton.remove()
    levelLabel.remove()
    targetLabel.remove()
    stepsLabel.remove()
    winnerLabel.remove()

    updateLevelText()
    updateTargetText()
    updateStepsText()
    levelLabel.setPosition(Gdx.graphics.width.toFloat() - levelLabel.width, Gdx.graphics.height.toFloat() - levelLabel.height * TEXT_SCALE_FACTOR - 25)
    targetLabel.setPosition(Gdx.graphics.width.toFloat() - targetLabel.width, levelLabel.y - 150)
    stepsLabel.setPosition(Gdx.graphics.width.toFloat() - stepsLabel.width, targetLabel.y - 150)

    stage.addActor(resetButton)
    stage.addActor(undoButton)
    stage.addActor(levelLabel)
    stage.addActor(targetLabel)
    stage.addActor(stepsLabel)
  }
}
