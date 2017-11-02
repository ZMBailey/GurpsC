package soulstudios.gurpsc

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.content_add_ranged.*

class AddRangedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ranged)
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
            this@AddRangedActivity.finish()
        }
    }

    inner class AddButtonHandler: View.OnClickListener {
        override fun onClick(v: View?) {
            val w_class = wClass.text.toString()
            val name = wName.text.toString()
            val dmg = wDmg.text.toString()
            val acc = wAcc.text.toString()
            val range = wRange.text.toString()
            val rof = wRof.text.toString()
            val shots = wShots.text.toString()
            val rcl = wRcl.text.toString()
            val cost = ""

            App.current.addRanged(name,w_class,dmg,acc,range,rof,shots,rcl)
            val added = "Weapon $name has been added"
            Toast.makeText(this@AddRangedActivity, added,
                    Toast.LENGTH_SHORT).show( )
            this@AddRangedActivity.finish()
        }
    }
}