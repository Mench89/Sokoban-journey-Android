package magnus.com.sokoban.start

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ListView
import magnus.com.sokoban.R
import magnus.com.sokoban.menu.MenuItem

/**
 * Activity for handling start menu with different options.
 */
class StartMenuActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_menu)

    val menuList = findViewById<ListView>(R.id.menu_list)
    val menuItems = ArrayList<MenuItem>()
    menuItems.add(MenuItem("Start game", null))
    menuItems.add(MenuItem("Exit", View.OnClickListener {
      Log.d("StartMenuActivity", "Exiting game. Good bye!")
      finish()
    }))
    menuList.adapter = StartMenuItemsAdapter(this, menuItems)
    menuList.setOnItemClickListener { parent, view, position, _ ->
      val item = parent?.getItemAtPosition(position) as MenuItem
      item.clickListener?.onClick(view)
    }
  }
}
