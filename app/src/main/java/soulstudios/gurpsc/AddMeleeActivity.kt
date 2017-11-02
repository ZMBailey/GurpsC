package soulstudios.gurpsc

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import kotlinx.android.synthetic.main.content_add_melee.*

class AddMeleeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_melee)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        updateActivity()
    }

    private fun updateActivity(){
        add_back.setOnClickListener(BackHandler())
        add_add.setOnClickListener(AddButtonHandler())
    }

    inner class BackHandler: View.OnClickListener {
        override fun onClick(v: View?) {
            this@AddMeleeActivity.finish()
        }
    }

    inner class AddButtonHandler: View.OnClickListener {
        override fun onClick(v: View?) {
            val w_class = wClass.text.toString()
            val name = wName.text.toString()
            val dmg = wDmg.text.toString()
            val reach = wReach.text.toString()
            val parry = wParry.text.toString()

            App.current.addMelee(name,w_class,dmg,reach,parry)
            val added = "Weapon $name has been added"
            Toast.makeText(this@AddMeleeActivity, added,
                    Toast.LENGTH_SHORT).show( )
            this@AddMeleeActivity.finish()
        }
    }
}