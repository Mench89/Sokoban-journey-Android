package magnus.com.sokoban.start

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import magnus.com.sokoban.R
import magnus.com.sokoban.menu.MenuItem

/**
 * Adapter for showing rows in start menu.
 */
class StartMenuItemsAdapter(context: Context, val menuItems: List<MenuItem>)
  : ArrayAdapter<MenuItem>(context, R.layout.menu_list_item, menuItems) {

  private val inflater = LayoutInflater.from(context)

  override fun getCount(): Int {
    return menuItems.count()
  }

  override fun getItem(position: Int): MenuItem {
    return menuItems[position];
  }

  override fun getItemId(position: Int): Long {
    return position.toLong()
  }

  override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
    val view: View?
    val viewHolder: MenuListRowViewHolder
    if (convertView == null) {
      view = inflater.inflate(R.layout.menu_list_item, parent, false)
      viewHolder = MenuListRowViewHolder(view)
      view.tag = viewHolder
    } else {
      view = convertView
      viewHolder = view.tag as MenuListRowViewHolder
    }

    viewHolder.label?.text = menuItems[position].itemText
    return view
  }

  /**
   * View holder for a menu item row.
   */
  private class MenuListRowViewHolder(row: View?) {
    val label = row?.findViewById<TextView>(R.id.menu_item_text)
  }
}