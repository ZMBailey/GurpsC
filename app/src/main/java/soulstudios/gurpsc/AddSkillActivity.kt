package soulstudios.gurpsc

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.content_add_skill.*

class AddSkillActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_add_skill)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        updateActivity()
    }

    fun updateActivity(){
        add_back.setOnClickListener(BackHandler())
        add_add.setOnClickListener(AddButtonHandler())
        sdiff.onItemSelectedListener = DiffHandler()
        sattr.onItemSelectedListener = AttrHandler()
    }

    inner class BackHandler:View.OnClickListener{
        override fun onClick(v: View?) {
            this@AddSkillActivity.finish()
        }
    }

    inner class AddButtonHandler:View.OnClickListener{
        override fun onClick(v: View?) {
            val name = sname.text.toString()
            val spec = sspec.text.toString()
            val note = ""
            App.current.addSkill(name,attr,spec,note,diff,1)
            val added = "Skill $name has been added"
            Toast.makeText( this@AddSkillActivity, added,
                    Toast.LENGTH_SHORT ).show( )
            this@AddSkillActivity.finish()
        }
    }

    inner class DiffHandler: AdapterView.OnItemSelectedListener{
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            diff = difficulty[parent!!.getItemAtPosition(position).toString()]!!
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    inner class AttrHandler: AdapterView.OnItemSelectedListener{
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            attr = parent!!.getItemAtPosition(position).toString()
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
}
