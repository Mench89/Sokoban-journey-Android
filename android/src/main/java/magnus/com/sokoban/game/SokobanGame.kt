package magnus.com.sokoban.game;

import android.util.Log
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import java.util.*

// TODO: Select levels
// TODO: Add swipe input
// TODO: Map editor?
// TODO: Animations
// TODO: Fix reset
// TODO: Add history and fix undo
class SokobanGame : ApplicationAdapter(), TargetStateModel.GameStateListener, MovementHandler.MovementListener {

  override fun onPlayerMoved() {
    targetStateModel.update()
    Log.d("Sokoban game", "Number of reached targets: " + targetStateModel.numberOfReachedTargets)
    targetLabel.setText(targetStateModel.numberOfReachedTargets.toString())
  }

  override fun onAllTargetsReached() {
    Log.d("Sokoban game", "WINNER WINNER TACO DINNER!")
    stage.addActor(winnerLabel)
  }

  // TODO: Stuffs to remain here...
  lateinit var batch: SpriteBatch
  lateinit var skin: Skin
  lateinit var stage: Stage
  lateinit var winnerLabel: Label
  lateinit var targetLabel: Label
  lateinit var level: Level
  lateinit var movementHandler: MovementHandler
  lateinit var targetStateModel: TargetStateModel
  // TODO: Only for test
  lateinit var levelParser: LevelParser

  override fun create() {
    batch = SpriteBatch()
    stage = Stage()
    skin = Skin(Gdx.files.internal("skin/uiskin.json"))

    targetLabel = Label("0", skin)
    targetLabel.setFontScale(4F)
    targetLabel.setPosition(Gdx.graphics.width.toFloat() - targetLabel.width * 4F, Gdx.graphics.height.toFloat() - targetLabel.height*4F)

    winnerLabel = Label("WINNER WINNER TACO DINNER!", skin)
    winnerLabel.setFontScale(4F)
    winnerLabel.setPosition(Gdx.graphics.width / 2 - (winnerLabel.width * 4F / 2), Gdx.graphics.height / 2 + (winnerLabel.height * 4F / 2))

    initWorld()

    val resetButton = TextButton("Reset", skin)
    resetButton.isTransform = true
    resetButton.setScale(3F, 3F)
    resetButton.setColor(1F,0F,0F,1F)
    resetButton.setPosition(Gdx.graphics.width.toFloat() - resetButton.width * 3F, 0F)
    resetButton.addListener {
      initWorld()
      initInputHandlers()
      true
    }
    val undoButton = TextButton("Undo", skin)
    undoButton.isTransform = true
    undoButton.setScale(3F, 3F)
    undoButton.setColor(0F,1F,0F,1F)
    undoButton.setPosition(Gdx.graphics.width.toFloat() - undoButton.width * 3F, resetButton.height*4)

    stage.addActor(resetButton)
    stage.addActor(undoButton)
    stage.addActor(targetLabel)

    initInputHandlers()
  }

  override fun render() {
    batch.begin()
    level.draw(batch)
    batch.end()
    stage.draw()
  }

  override fun dispose() {
    batch.dispose()
  }

  private fun initWorld() {
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
    level = LevelParser.parseLevel("world1/1-10.xml")
    winnerLabel.remove()
    targetLabel.setText("0")
    targetStateModel = TargetStateModel(level.boxes, level.targets, this)
  }

  private fun initInputHandlers() {
    movementHandler = MovementHandler(level, this)
    val inputMultiplexer = InputMultiplexer()
    inputMultiplexer.addProcessor(stage)
    inputMultiplexer.addProcessor(movementHandler.gestureDetector)
    Gdx.input.inputProcessor = inputMultiplexer
  }
}
