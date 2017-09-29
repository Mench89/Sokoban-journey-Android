package magnus.com.sokoban.game;

import android.util.Log
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton

class SokobanGame : ApplicationAdapter(), InputHandler.UserInteractionListener, TargetStateModel.GameStateListener {
  override fun onAllTargetsReached() {
    Log.d("Sokoban game", "WINNER WINNER TACO DINNER!")
    stage.addActor(winnerLabel)
  }

  override fun onUserTapped(tappedPoint: Vector3) {
    var newPosXDiff = 0
    var newPosyDiff = 0
    val screenXRelativePlayerX = tappedPoint.x - (player.getX().toInt() + (player.shape.width / 2))
    val screenXRelativePlayerY = tappedPoint.y - (player.getY().toInt() + (player.shape.height / 2))

    if (Math.abs(screenXRelativePlayerX) > Math.abs(screenXRelativePlayerY)) {
      if(screenXRelativePlayerX > 0) {
        newPosXDiff = 96
        Log.d("Sokoban game", "Going right")
      } else {
        newPosXDiff = -96
        Log.d("Sokoban game", "Going left")
      }
    } else {
      if(screenXRelativePlayerY > 0) {
        newPosyDiff = 96
        Log.d("Sokoban game", "Going up")
      } else {
        newPosyDiff = -96
        Log.d("Sokoban game", "Going down")
      }
    }
    // TODO: Validate if positoin is valid to move to.
    player.setPosition(player.getX() + newPosXDiff, player.getY() + newPosyDiff)
    if(CollisionHelper.isCollidingWithAny(player.shape, walls.wallShapes)) {
      Log.d("Sokoban game", "Player went into a wall!")
      // Player went into a wall! Revert player position.
      player.setPosition(player.getX() - newPosXDiff, player.getY() - newPosyDiff)
    } else {
      checkForBoxCollision(newPosXDiff, newPosyDiff)
    }
  }

  private fun checkForBoxCollision(newPosXDiff : Int, newPosYDiff : Int) {
    // TODO: Handle new player pos if code above is changed.
    if(CollisionHelper.isColliding(player.shape, box.shape)) {
      box.setPosition(box.getX() + newPosXDiff, box.getY() + newPosYDiff)

      if(CollisionHelper.isCollidingWithAny(box.shape, walls.wallShapes)) {
        // Revert player and box positions.
        player.setPosition(player.getX() - newPosXDiff, player.getY() - newPosYDiff)
        box.setPosition(box.getX() - newPosXDiff, box.getY() - newPosYDiff)
        return
      }
    }
  }

  lateinit var batch: SpriteBatch
  lateinit var player: Player
  lateinit var world: World
  lateinit var walls: Walls
  lateinit var floor: Floor
  lateinit var box: Box
  lateinit var target: Target
  lateinit var targetStateModel: TargetStateModel
  lateinit var stage: Stage
  lateinit var targetLabel: Label
  lateinit var skin: Skin
  lateinit var winnerLabel: Label

  override fun create() {
    batch = SpriteBatch()
    stage = Stage()
    skin = Skin(Gdx.files.internal("skin/uiskin.json"))

    winnerLabel = Label("WINNER WINNER TACO DINNER!", skin)
    winnerLabel.setFontScale(4F)
    winnerLabel.setPosition(Gdx.graphics.width / 2 - (winnerLabel.width * 4F / 2), Gdx.graphics.height / 2 + (winnerLabel.height * 4F / 2))

    initWorld()

    val camera = OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    camera.setToOrtho(false, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())

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
    targetLabel = Label("0", skin)
    targetLabel.setFontScale(4F)
    targetLabel.setPosition(Gdx.graphics.width.toFloat() - targetLabel.width * 4F, Gdx.graphics.height.toFloat() - targetLabel.height*4F)
    //stage = Stage()
    stage.addActor(resetButton)
    stage.addActor(undoButton)
    stage.addActor(targetLabel)
    val inputMultiplexer = InputMultiplexer()
    inputMultiplexer.addProcessor(stage)
    inputMultiplexer.addProcessor(GestureDetector(InputHandler(camera, this)))
    Gdx.input.inputProcessor = inputMultiplexer
  }

  override fun render() {
    targetStateModel.update()
    Log.d("Sokoban game", "Number of reached targets: " + targetStateModel.numberOfReachedTargets)
    targetLabel.setText(targetStateModel.numberOfReachedTargets.toString())

    batch.begin()
    world.draw(batch)
    floor.draw(batch)
    walls.draw(batch)
    target.draw(batch)
    box.draw(batch)
    player.draw(batch)
    batch.end()

    stage.draw()
  }

  override fun dispose() {
    batch.dispose()
  }

  private fun initWorld() {
    player = Player()
    world = World()
    walls = Walls()
    floor = Floor()
    box = Box()
    box.setPosition(WorldConstants.CELL_SIZE, WorldConstants.CELL_SIZE * 4)
    target = Target()
    target.setPosition(WorldConstants.CELL_SIZE, WorldConstants.CELL_SIZE * 8)
    targetStateModel = TargetStateModel(arrayOf(box), arrayOf(target), this)
    winnerLabel.remove()
  }
}
