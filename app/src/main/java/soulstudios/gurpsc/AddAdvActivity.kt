package soulstudios.gurpsc

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.CompoundButton
import android.widget.Toast
import kotlinx.android.synthetic.main.content_add_adv.*

class AddAdvActivity : AppCompatActivity() {

    var diff:String = "Ea"
    var attr:String = "Str"

    val difficulty:HashMap<String,String> = hashMapOf(
            "Easy" to "Ea",
            "Average" to "Av",
            "Hard" to "Hd",
            "Very Hard" to "Vh"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_adv)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        updateActivity()
    }

    fun updateActivity(){
        add_back.setOnClickListener(BackHandler())
        but_add_a.setOnClickListener(AddButtonHandler())
        ahaslvl.setOnCheckedChangeListener(HasLvlsHandler())
    }

    inner class BackHandler: View.OnClickListener {
        override fun onClick(v: View?) {
            this@AddAdvActivity.finish()
        }
    }

    inner class AddButtonHandler: View.OnClickListener {
        override fun onClick(v: View?) {
            val name = aname.text.toString()
            val desc = adesc.text.toString()
            var cost = 0
            var lcost = 0
            var lvl = 0
            val hasLvl = ahaslvl.isChecked
            if(hasLvl){
                lcost = acostllvl.text.toString().toInt()
                lvl = alvls.text.toString().toInt()
            }else {
                cost = acost.text.toString().toInt()
            }
            Log.w("Checked",hasLvl.toString())
            App.current.addAdv(name,cost,hasLvl,lcost,lvl,desc)
            val added = "Skill $name has been added"
            Toast.makeText(this@AddAdvActivity, added,
                    Toast.LENGTH_SHORT).show( )
            this@AddAdvActivity.finish()
        }
    }

    inner class HasLvlsHandler: CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            if(isChecked){
                Log.w("Checked","True")
                acost.isEnabled = false
                acostllvl.isEnabled = true
                alvls.isEnabled = true
            }else{
                Log.w("Checked","False")
                acost.isEnabled = true
                acostllvl.isEnabled = false
                alvls.isEnabled = false
            }
        }
    }
}