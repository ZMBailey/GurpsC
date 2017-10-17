package soulstudios.gurpsc

import android.graphics.Point
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.Toast
import io.objectbox.kotlin.boxFor
import kotlinx.android.synthetic.main.content_load.*
import soulstudios.gurpsc.App.Companion.current

class LoadActivity : AppCompatActivity() {

    private val gc =  GChar::class
    private var charBox = App.boxStore.boxFor(gc)
    private var buttonWidth: Int = 0
    private var buttons: MutableMap<Button,Long> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        var size = Point()
        windowManager.defaultDisplay.getSize(size)
        buttonWidth = size.x
        updateView()
    }

    private fun updateView( ) {
        val chars: MutableList<GChar> = charBox.all
        if( chars.size > 0 ) {
            char_list.removeAllViewsInLayout( )

            val grid = GridLayout(this)
            grid.rowCount = chars.size
            grid.columnCount = 2

            val button: Array<Button> =Array<Button>(chars.size,{i -> Button(this)})
            val delete: Array<Button> =Array<Button>(chars.size,{i -> Button(this)})
            val bh = ButtonHandler()
            val dh = DeleteHandler()

            var lp:GridLayout.LayoutParams = GridLayout.LayoutParams()

            for ((i,gc) in chars.withIndex()) {
                button[i].text = gc.name
                button[i].background = resources.getDrawable(R.drawable.rounded_green,null)
                button[i].setTextColor(resources.getColor(android.R.color.black,null))
                button[i].setOnClickListener( bh )
                buttons.put(button[i],gc.id)

                delete[i].text = resources.getText(R.string.but_delete)
                delete[i].setTextColor(resources.getColor(android.R.color.black,null))
                delete[i].background = resources.getDrawable(R.drawable.rounded_purple,null)
                delete[i].setOnClickListener( dh )
                buttons.put(delete[i],gc.id)

                grid.addView( button[i], (buttonWidth*.65).toInt(),
                        GridLayout.LayoutParams.WRAP_CONTENT )
                grid.addView( delete[i], (buttonWidth*.35).toInt(),
                        GridLayout.LayoutParams.WRAP_CONTENT )
            }

            //create back button
            val back = Button(this)
            back.text = resources.getText(R.string.but_back)
            back.background = resources.getDrawable(android.R.color.background_dark,null)
            back.setTextColor(resources.getColor(android.R.color.white,null))
            back.setOnClickListener(){ v -> this@LoadActivity.finishAfterTransition() }
            grid.addView( back, buttonWidth/2,
                    GridLayout.LayoutParams.WRAP_CONTENT )

            char_list.addView( grid )
        }
    }

    inner class ButtonHandler: View.OnClickListener {
        override fun onClick( v:View ) {

            val temp:GChar = charBox.get(buttons[v]!!)
            App.current.load(temp)
            val selected: String = temp.name + " Loaded"
            Toast.makeText( this@LoadActivity, selected,
                    Toast.LENGTH_SHORT ).show(
            )
            this@LoadActivity.finish()
        }
    }

    inner class DeleteHandler: View.OnClickListener {
        override fun onClick( v:View ) {
            charBox.remove(buttons[v]!!)
            val selected: String = "Character Deleted"
            Toast.makeText( this@LoadActivity, selected,
                    Toast.LENGTH_SHORT ).show( )
            this@LoadActivity.updateView()
        }
    }
}
