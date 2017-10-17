package soulstudios.gurpsc

import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.transition.Explode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import io.objectbox.kotlin.boxFor
import kotlinx.android.synthetic.main.content_load.*
import kotlinx.android.synthetic.main.fragment_options.view.*
import kotlinx.android.synthetic.main.fragment_skills.*
import soulstudios.gurpsc.App.Companion.boxStore
import soulstudios.gurpsc.App.Companion.current

/**
 * Created by soulo_000 on 9/28/2017.
 */
class PageFragment : Fragment() {

    private var this_page = 0
    private var rView: View? = null
    private var isCreated: Boolean = false
    var charBox = App.boxStore.boxFor(GChar::class.java)
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(this_page, container, false)
        rView = rootView

        when(this_page){
            R.layout.fragment_options -> {Log.w("frag:","Options");setup("Options",rootView)}
            R.layout.fragment_description -> {setup("Description",rootView);Log.w("frag:","D")}
            R.layout.fragment_attributes -> {setup("Attributes",rootView);Log.w("frag:","A")}
            R.layout.fragment_reference -> {setup("Reference",rootView);Log.w("frag:","R")}
        }

        return rootView
    }

    override fun onStop() {
        super.onStop()
        Log.w("Run","OnStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.w("Run","OnDestroy")
    }

    fun updateView(type: String){
        Log.w("Updating","View")
        Log.w("Current attr",App.current.attributes.toString())
        if(page == R.layout.fragment_skills){
            setSkills()
        }else {
            setFragment(type)
        }
    }

    private fun setup(type: String,rootView: View){
        if(!rViews.values.contains(rootView)) {
            rViews.put(type,rootView)
        }

        if(type == "Options") {
            setOptions()
        }
        else{
            setHandlers(type)
        }
    }

    private fun setOptions(){
        rViews["Options"]!!.button_new.setOnClickListener(NewHandler())
        rViews["Options"]!!.button_load.setOnClickListener(LoadHandler())
        rViews["Options"]!!.button_save.setOnClickListener(SaveHandler())
    }

    inner class NewHandler: View.OnClickListener{
        override fun onClick(v: View?) {
            App.current = GCharacter()
            Log.w("New",App.current.attributes.toString())
            val new = "New character created"
            Toast.makeText( this@PageFragment.context, new,
                    Toast.LENGTH_SHORT ).show( )
        }
    }

    inner class SaveHandler: View.OnClickListener{
        override fun onClick(v: View?) {
            //var charBox = App.boxStore.boxFor(GChar::class.java)
            val boxid:Long = App.current.id
            if(current.id != 0L) {
                if (charBox.get(current.id) != null) {
                    charBox.remove(current.id)
                }
            }
            charBox.put(App.current.export())
            val box:MutableList<GChar> = charBox.all
            if(boxid == 0L){ App.current.load(box[box.size-1])}
            val name = current.getDesc("Name")
            val saved = "Character $name has been saved."
            Toast.makeText( this@PageFragment.context, saved,
                    Toast.LENGTH_SHORT ).show( )
        }
    }

    inner class LoadHandler: View.OnClickListener{
        override fun onClick(v: View?) {
            win!!.exitTransition = Explode()
            val select = Intent(this@PageFragment.context,LoadActivity::class.java)
            startActivity(select)
        }
    }

    inner class AddHandler: View.OnClickListener{
        override fun onClick(v: View?) {
            win!!.exitTransition = Explode()
            val select = Intent(this@PageFragment.context,AddSkillActivity::class.java)
            startActivity(select)
        }
    }

    private fun setHandlers(type: String){
        val inputs = InputArray(type,rViews[type]!!)
        for (v:TextView in inputs.inputs!!){
            v.addTextChangedListener(TextHandler(v))
        }
    }

    fun setP(p: Int){
        this_page = p
    }

    fun setSkills(){
        skill_Layout.removeAllViewsInLayout( )
        val grid = Skilltable.newInstance(rView,resources)
        val add = Button(rView!!.context)
        add.text = resources.getText(R.string.but_newskill)
        add.background = resources.getDrawable(android.R.color.background_dark,null)
        add.setTextColor(resources.getColor(android.R.color.white,null))
        add.setOnClickListener(AddHandler())
        val ap = GridLayout.LayoutParams()
        grid.setParams(App.current.getSkills().size+2,0,ap)
        grid.addView( add, ap)
        skill_Layout.addView( grid )
    }

    internal fun setFragment(type: String) {
        if(page == this_page) {
            val inputs = InputArray(type, rViews[type]!!)
            for (v: TextView in inputs.inputs!!) {
                //if (v.id != curr) {
                Log.w(App.current.name[v.id], App.current.getAttrById(v.id))
                if (type == "Description") {
                    v.text = App.current.getDescById(v.id)
                } else {
                    v.text = App.current.getAttrById(v.id)
                }
                //}
            }
        }
    }

    inner class TextHandler(p: TextView): TextWatcher
    {
        private val id: Int = p.id
        private val sec: String = p.tag.toString()

        override fun afterTextChanged(s: Editable?) {
            if((s.toString() != App.current.getAttrById(id)) && s.toString() != "" && page == this_page) {
                when (sec) {
                    "Desc" -> App.current.setDesc(s.toString(), id)
                    "Attr" -> {
                        val name = App.current.name[id]
                        Log.w("$name changed to",s.toString())
                        if(id == R.id.speed_input){
                            App.current.setById(id, s.toString().toFloat(), 0, 0)
                            setFragment("Attributes")
                        }else {
                            App.current.setById(id, s.toString().toInt(), 0, 0)
                            if(s.toString().toInt() > 2){
                                setFragment("Attributes")
                            }
                        }
                    }
                    "Ref" -> {App.current.setById(id, Integer.parseInt(s.toString()), 0, 0)
                        setFragment("Reference") }
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private val ARG_SECTION_NUMBER = "section_number"
        val fragments = intArrayOf(
                R.layout.fragment_options,
                R.layout.fragment_description,
                R.layout.fragment_attributes,
                R.layout.fragment_reference,
                R.layout.fragment_skills)
        var win:Window? = null
        var rViews: MutableMap<String,View> = mutableMapOf()
        var page: Int = 0
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        fun newInstance(sectionNumber: Int,w: Window): PageFragment {
            win = w
            val fragment = PageFragment()
            val args = Bundle()
            args.putInt(ARG_SECTION_NUMBER, sectionNumber)
            fragment.arguments = args
            fragment.setP(fragments[sectionNumber-1])
//            Log.w("Position",(fragments[sectionNumber-1]).toString())
            return fragment
        }

    }
}