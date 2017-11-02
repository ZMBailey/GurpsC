package soulstudios.gurpsc

import android.graphics.Point
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.Toast
import io.objectbox.kotlin.boxFor
import kotlinx.android.synthetic.main.content_load.*

class LoadActivity : AppCompatActivity() {

    private val gc =  GCharacter::class
    private val sk = Skill::class
    private var charBox = App.boxStore.boxFor(gc)
    private var meleeBox = App.boxStore.boxFor(Melee::class)
    private var rangedBox = App.boxStore.boxFor(Ranged::class)
    private var skillBox = App.boxStore.boxFor(sk)
    private var buttonWidth: Int = 0
    private var buttons: MutableMap<Button,Long> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val size = Point()
        windowManager.defaultDisplay.getSize(size)
        buttonWidth = size.x
        updateView()
    }

    private fun updateView( ) {
        val chars: MutableList<GCharacter> = charBox.all
        char_list.removeAllViewsInLayout( )

        val grid = GridLayout(this)
        grid.rowCount = chars.size+1
        grid.columnCount = 2

        if( chars.size > 0 ) {

            val button = List(chars.size){ Button(this)}
            val delete = List(chars.size){ Button(this)}
            val bh = ButtonHandler()
            val dh = DeleteHandler()

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
        }

        //create back button
        val back = Button(this)
        back.text = resources.getText(R.string.but_back)
        back.background = resources.getDrawable(android.R.color.background_dark,null)
        back.setTextColor(resources.getColor(android.R.color.white,null))
        back.setOnClickListener{ _ -> this@LoadActivity.finishAfterTransition() }
        grid.addView( back, buttonWidth/2,
                GridLayout.LayoutParams.WRAP_CONTENT )

        char_list.addView( grid )
    }

    inner class ButtonHandler: View.OnClickListener {
        override fun onClick( v:View ) {

            App.current = charBox.get(buttons[v]!!)
            App.current.load()
            val selected: String = App.current.name + " Loaded"
            Log.w("Skills",App.current.getSkillList())
            Log.w("LoadAttr",App.current.attr)
            Log.w("FullAttr",App.current.attributes.toString())
            Toast.makeText( this@LoadActivity, selected,
                    Toast.LENGTH_SHORT ).show(
            )
            this@LoadActivity.finish()
        }
    }

    inner class DeleteHandler: View.OnClickListener {
        override fun onClick( v:View ) {
            charBox.remove(buttons[v]!!)
            val selected = "Character Deleted"
            Toast.makeText( this@LoadActivity, selected,
                    Toast.LENGTH_SHORT ).show( )
            this@LoadActivity.updateView()
        }
    }
}
