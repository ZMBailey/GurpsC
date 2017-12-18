package soulstudios.gurpsc

import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
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
import android.widget.*
import io.objectbox.kotlin.boxFor
import kotlinx.android.synthetic.main.content_load.*
import kotlinx.android.synthetic.main.fragment_advantages.*
import kotlinx.android.synthetic.main.fragment_options.view.*
import kotlinx.android.synthetic.main.fragment_skills.*
import kotlinx.android.synthetic.main.fragment_weapons.*
import soulstudios.gurpsc.App.Companion.boxStore
import soulstudios.gurpsc.App.Companion.current

/**PageFragment
 * Each PageFragment is a page of the character sheet.
 * Current Pages are:
 * -Options
 * -Description
 * -Attributes
 * -Reference
 * -Skills
 * Planned Pages:
 * -Weapon_Skills
 * -Advantages/Disadvantages
 */
class PageFragment : Fragment() {

    private var this_page = 0
    private var rView: View? = null
    private var skillAdded: Boolean = false
    private var weaponAdded: Boolean = false
    private var advAdded: Boolean = false
    var skillBox = App.boxStore.boxFor(Skill::class)
    var meleeBox = App.boxStore.boxFor(Melee::class)
    var rangedBox = App.boxStore.boxFor(Ranged::class)
    var charBox = App.boxStore.boxFor(GCharacter::class)
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

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
    }

    //onResume, if returning from the addskills screen, reload the skills table
    override fun onResume() {
        super.onResume()
        when{
            skillAdded -> {
                setSkills()
                skillAdded = false
            }
            weaponAdded -> {
                setWeapons()
                weaponAdded = false
            }
            advAdded -> {
                setAdvs()
                advAdded = false
            }
        }
        updateTitle()
    }

    //when switching to a new page, reload field entries for the
    //new page
    fun updateView(type: String){
        Log.w("Updating","View")
        Log.w("Current attr",App.current.attributes.toString())
        when {
            (page == R.layout.fragment_skills) -> setSkills()
            (page == R.layout.fragment_weapons) -> setWeapons()
            (page == R.layout.fragment_advantages) -> setAdvs()
            else -> setFragment(type)
        }
        updateTitle()
    }

    //initial setup for a page, differs depending on the page
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

    //set handlers for the option buttons
    private fun setOptions(){
        rViews["Options"]!!.button_new.setOnClickListener(NewHandler())
        rViews["Options"]!!.button_load.setOnClickListener(LoadHandler())
        rViews["Options"]!!.button_save.setOnClickListener(SaveHandler())
    }

    //handler for the New Character button
    inner class NewHandler: View.OnClickListener{
        override fun onClick(v: View?) {
            App.current = GCharacter()
            Log.w("New",App.current.attributes.toString())
            val new = "New character created"
            Toast.makeText( this@PageFragment.context, new,
                    Toast.LENGTH_SHORT ).show( )
        }
    }

    //handler for the Save button
    inner class SaveHandler: View.OnClickListener{
        override fun onClick(v: View?) {
            App.current.export()
            charBox.put(App.current)
            Log.w("new Id",App.current.id.toString())
            val name = current.getDesc("Name")
            val saved = "Character $name has been saved."
            Toast.makeText( this@PageFragment.context, saved,
                    Toast.LENGTH_SHORT ).show( )
        }
    }

    //handler for the Load button
    inner class LoadHandler: View.OnClickListener{
        override fun onClick(v: View?) {
            win!!.exitTransition = Explode()
            val select = Intent(this@PageFragment.context,LoadActivity::class.java)
            startActivity(select)
        }
    }

    //handler for the Add Skill button
    inner class AddSkillHandler: View.OnClickListener{
        override fun onClick(v: View?) {
            win!!.exitTransition = Explode()
            skillAdded=true
            val select = Intent(this@PageFragment.context,AddSkillActivity::class.java)
            startActivity(select)
        }
    }

    //handler for the Add Advantage button
    inner class AddAdvHandler: View.OnClickListener{
        override fun onClick(v: View?) {
            win!!.exitTransition = Explode()
            advAdded=true
            val select = Intent(this@PageFragment.context,AddAdvActivity::class.java)
            startActivity(select)
        }
    }

    //handler for the Add Melee button
    inner class AddMllHandler: View.OnClickListener{
        override fun onClick(v: View?) {
            win!!.exitTransition = Explode()
            weaponAdded=true
            val select = Intent(this@PageFragment.context,AddMeleeActivity::class.java)
            startActivity(select)
        }
    }

    //handler for the Add Ranged button
    inner class AddRangedHandler: View.OnClickListener{
        override fun onClick(v: View?) {
            win!!.exitTransition = Explode()
            weaponAdded=true
            val select = Intent(this@PageFragment.context,AddRangedActivity::class.java)
            startActivity(select)
        }
    }

    //set listeners for text fields
    private fun setHandlers(type: String){
        val inputs = InputArray(type,rViews[type]!!)
        for (v:TextView in inputs.inputs!!){
            v.addTextChangedListener(TextHandler(v))
        }
    }

    //track the current page
    fun setP(p: Int){
        this_page = p
    }

    //re-build the skills table
    fun setSkills(){
        skill_Layout.removeAllViewsInLayout( )
        val grid = Skilltable.newInstance(rView,this)
        val add = Button(rView!!.context)
        add.text = resources.getText(R.string.but_newskill)
        add.background = resources.getDrawable(android.R.color.background_dark,null)
        add.setTextColor(resources.getColor(android.R.color.white,null))
        add.setOnClickListener(AddSkillHandler())
//        val ap = GridLayout.LayoutParams()
//        grid.setParams(App.current.skills.size+2,0,ap)
        grid.addView( add)
        skill_Layout.addView( grid )
    }

    //re-build the melee table
    fun setWeapons(){
        Weapon_Tables.removeAllViewsInLayout( )
        val grid_m = Weapontable.newMelee(rView,resources.getString(R.string.melee_title),this)
        val addm = Button(rView!!.context)
        addm.text = resources.getText(R.string.but_newmelee)
        addm.background = resources.getDrawable(android.R.color.background_dark,null)
        addm.setTextColor(resources.getColor(android.R.color.white,null))
        addm.setOnClickListener(AddMllHandler())
        grid_m.addView( addm)
        Weapon_Tables.addView( grid_m )

        val grid_r = Weapontable.newRanged(rView,resources.getString(R.string.ranged_title),this)
        val addr = Button(rView!!.context)
        addr.text = resources.getText(R.string.but_newranged)
        addr.background = resources.getDrawable(android.R.color.background_dark,null)
        addr.setTextColor(resources.getColor(android.R.color.white,null))
        addr.setOnClickListener(AddRangedHandler())
        grid_r.addView( addr)
        grid_r.setPadding(0,10,0,0)
        Weapon_Tables.addView( grid_r )
    }

    //re-build the advantages table
    fun setAdvs(){
        adv_Layout.removeAllViewsInLayout( )
        val grid = Advantagetable.newInstance(rView,resources.getString(R.string.advantages),this)
        val add = Button(rView!!.context)
        add.text = resources.getText(R.string.but_add_a)
        add.background = resources.getDrawable(android.R.color.background_dark,null)
        add.setTextColor(resources.getColor(android.R.color.white,null))
        add.setOnClickListener(AddAdvHandler())
//        val ap = GridLayout.LayoutParams()
//        grid.setParams(App.current.skills.size+2,0,ap)
        grid.addView( add)
        adv_Layout.addView( grid )
    }

    //set text fields in the current page
    internal fun setFragment(type: String) {
        if(page == this_page) {
            val inputs = InputArray(type, rViews[type]!!)
            for (v: TextView in inputs.inputs!!) {
                //if (v.id != curr) {
                Log.w(App.current.index[v.id], App.current.getAttrById(v.id))
                if (type == "Description") {
                    v.text = App.current.getDescById(v.id)
                } else {
                    v.text = App.current.getAttrById(v.id)
                }
                //}
            }
        }
    }

    fun updateTitle(){
        val parent = activity
        if(parent is CharSheetActivity) {
            parent.setPoints()
            parent.setChar()
        }
    }

    /*handler for the text fields
    updates other text fields that have been altered by the new input
     */
    inner class TextHandler(p: TextView): TextWatcher
    {
        private val id: Int = p.id
        private val sec: String = p.tag.toString()

        override fun afterTextChanged(s: Editable?) {
            if((s.toString() != App.current.getAttrById(id)) && s.toString() != "" && page == this_page) {
                when (sec) {
                    "Desc" -> {
                        App.current.setDescById(s.toString(), id)
                        updateTitle()
                    }
                    "Attr" -> {
                        val name = App.current.index[id]
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
                        updateTitle()
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
                R.layout.fragment_skills,
                R.layout.fragment_weapons,
                R.layout.fragment_advantages)
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