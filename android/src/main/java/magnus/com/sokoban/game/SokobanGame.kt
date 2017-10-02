package magnus.com.sokoban.game;

import android.util.Log
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton

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

    movementHandler = MovementHandler(level, this)
    val inputMultiplexer = InputMultiplexer()
    inputMultiplexer.addProcessor(stage)
    inputMultiplexer.addProcessor(movementHandler.gestureDetector)
    Gdx.input.inputProcessor = inputMultiplexer
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
    val box = Box()
    box.setPosition(WorldConstants.CELL_SIZE, WorldConstants.CELL_SIZE * 4)
    val target = Target()
    target.setPosition(WorldConstants.CELL_SIZE, WorldConstants.CELL_SIZE * 8)
    level = Level(Player(), World(), Walls(), Floor(), arrayOf(box), arrayOf(target), this)
    winnerLabel.remove()
    targetLabel.setText("0")
    targetStateModel = TargetStateModel(level.boxes, level.targets, this)
  }
}
