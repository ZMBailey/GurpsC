package soulstudios.gurpsc


import android.content.Intent
import android.content.res.Configuration
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
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
import kotlinx.android.synthetic.main.fragment_advantages.*
import kotlinx.android.synthetic.main.fragment_attributes.view.*
import kotlinx.android.synthetic.main.fragment_options.view.*
import kotlinx.android.synthetic.main.fragment_skills.*
import kotlinx.android.synthetic.main.fragment_weapons.*
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
    private var index = 0
    private var rView: View? = null
    private var skillAdded: Boolean = false
    private var weaponAdded: Boolean = false
    private var advAdded: Boolean = false
    private var con: ViewGroup? = null
    var charBox = App.boxStore.boxFor(GCharacter::class)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val orient = activity.resources.configuration.orientation
        when(orient) {
            ORIENTATION_PORTRAIT -> PageFragment.fragments = verticalfragments
            ORIENTATION_LANDSCAPE -> fragments = horizontalfragments
        }
        this_page = fragments[index]
        con = container
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

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(!isVisibleToUser){
            when(this_page){
                R.layout.fragment_options -> Log.w("Not Visible","Options")
                R.layout.fragment_description -> {Log.w("Not Visible","Description");removeHandlers("Description")}
                R.layout.fragment_attributes -> {Log.w("Not Visible","Attributes");removeHandlers("Attributes")}
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.w("Run","OnDestroy")
    }

//    override fun onConfigurationChanged(newConfig: Configuration?) {
//        super.onConfigurationChanged(newConfig)
//        when(newConfig!!.orientation) {
//            ORIENTATION_PORTRAIT -> {
//                fragments = verticalfragments
//                when(this_page){
//                    R.layout.fragment_options_horizontal -> rView = layoutInflater.inflate(R.layout.fragment_options,con,false)
//                    R.layout.fragment_attributes_horizontal -> rView = layoutInflater.inflate(R.layout.fragment_attributes,con,false)
//                }
//            }
//            ORIENTATION_LANDSCAPE -> {
//                fragments = horizontalfragments
//                when(this_page){
//                    R.layout.fragment_options -> rView = layoutInflater.inflate(R.layout.fragment_options_horizontal,con,false)
//                    R.layout.fragment_attributes -> rView = layoutInflater.inflate(R.layout.fragment_attributes_horizontal,con,false)
//                }
//            }
//        }
//        this_page = fragments[index]
//    }

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

        when(type) {
            "Options" -> { setOptions() }
            "Attributes" -> {
                setRollButtons(type)
                setFragment(type)
                setHandlers(type)
            }
            else -> { setHandlers(type) }
        }
    }

    //set text fields in the current page
    internal fun setFragment(type: String) {
        if(page == this_page) {
            val inputs = InputArray(type, rView!!)
            for (v: TextView in inputs.inputs!!) {
                //if (v.id != curr) {
                Log.w(App.current.index[v.id], App.current.getAttrById(v.id))
                if (type == "Description") {
                    v.text = App.current.getDescById(v.id)
                } else {
                    v.removeTextChangedListener(TextHandler(v))
                    v.text = App.current.getAttrById(v.id)
                    v.addTextChangedListener(TextHandler(v))
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

    //set listeners for text fields
    private fun setHandlers(type: String){
        val inputs = InputArray(type,rViews[type]!!)
        for (v:TextView in inputs.inputs!!){
            v.addTextChangedListener(TextHandler(v))
        }
    }

    //set listeners for text fields
    private fun removeHandlers(type: String){
        if(rViews[type] != null) {
            val inputs = InputArray(type, rViews[type]!!)
            for (v: TextView in inputs.inputs!!) {
                v.removeTextChangedListener(TextHandler(v))
            }
        }
    }

    //set handlers for the option buttons
    private fun setOptions(){
        rViews["Options"]!!.button_new.setOnClickListener(NewHandler())
        rViews["Options"]!!.button_load.setOnClickListener(LoadHandler())
        rViews["Options"]!!.button_save.setOnClickListener(SaveHandler())
        rViews["Options"]!!.custom_roll.setOnClickListener(CustomRollHandler())
    }

    fun setRollButtons(type:String){
        val count = rViews[type]!!.basic_attrs.childCount
        var child:View
        for(i in 0..(count-1)){
            child = rViews[type]!!.basic_attrs.getChildAt(i)
            if(child is ImageButton){
                child.setOnClickListener(RollHandler())
            }
        }
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

    //track the current page
    fun setP(p: Int){
        index = p
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

    fun roll(target:Int){
        //val target:Int = App.current.getAttr(view.tag.toString()).toInt()
        val parent = activity
        if(parent is CharSheetActivity) {
            parent.rollDice(target)
        }
    }

    inner class CustomRollHandler: View.OnClickListener{
        override fun onClick(v: View?) {
            var target:Int = 0
            val input = rViews["Options"]!!.custom_input.text.toString()
            if(input != ""){
                target = input.toInt()
            }
            roll(target)
        }
    }

    inner class RollHandler: View.OnClickListener{
        override fun onClick(v: View?) {
            val target:Int = App.current.getAttr(v!!.tag.toString()).toInt()
            roll(target)
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
        val verticalfragments = intArrayOf(
                R.layout.fragment_options,
                R.layout.fragment_description,
                R.layout.fragment_attributes,
                R.layout.fragment_reference,
                R.layout.fragment_skills,
                R.layout.fragment_weapons,
                R.layout.fragment_advantages)

        val horizontalfragments = intArrayOf(
                R.layout.fragment_options_horizontal,
                R.layout.fragment_description,
                R.layout.fragment_attributes_horizontal,
                R.layout.fragment_reference,
                R.layout.fragment_skills,
                R.layout.fragment_weapons,
                R.layout.fragment_advantages)

        var fragments = verticalfragments
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
            fragment.setP(sectionNumber-1)
//            Log.w("Position",(fragments[sectionNumber-1]).toString())
            return fragment
        }

    }
}