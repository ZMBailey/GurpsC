package soulstudios.gurpsc

import android.content.res.Configuration
import android.database.CharArrayBuffer
import android.graphics.Point
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import io.objectbox.*

import android.widget.TextView
import kotlinx.android.synthetic.main.activity_char_sheet.*
import java.util.*

class CharSheetActivity : AppCompatActivity() {

    /**
     * The Parent activity containing the Tabs, the Die Roller, and the
     * ViewPager which displays all the pages.
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    /**
     * The [ViewPager] that will host the section contents.
     */
    private var mViewPager: ViewPager? = null
    private var pages: Array<PageFragment> = arrayOf()
    var page = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_char_sheet)

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        var size = Point()
        windowManager.defaultDisplay.getSize(size)
        App.width = size.x
        pt_total.addTextChangedListener(PointsHandler())

        // array of all pages, allows referencing of functions/variables specific to a page.
        pages = arrayOf(
                PageFragment.newInstance(1,window),
                PageFragment.newInstance(2,window),
                PageFragment.newInstance(3,window),
                PageFragment.newInstance(4,window),
                PageFragment.newInstance(5,window),
                PageFragment.newInstance(6,window),
                PageFragment.newInstance(7,window)
        )
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container) as ViewPager
        mViewPager!!.adapter = mSectionsPagerAdapter
        mViewPager!!.addOnPageChangeListener(PageListener())
        mViewPager!!.offscreenPageLimit = 0

        // Set up tabs
        tabs.setupWithViewPager(mViewPager)
        tabs.tabMode = TabLayout.MODE_SCROLLABLE
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_char_sheet, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        mSectionsPagerAdapter!!.notifyDataSetChanged()
    }

    //set the current character name in the header.
    fun setChar(){
        val charname = "Character: ${App.current.getDesc("name")}"
        character.text = charname
    }

    //display current unspent points
    fun setPoints(){
        App.current.recalcPoints()
        val points = "Unspent: ${App.current.showPoints().toString()}"
        pt_unspent.text = points
    }

    //Roll 3d6 and determine success failure based on input target.
    fun rollDice(target: Int){
        val d1 = genRandom(6,1)
        val d2 = genRandom(6,1)
        val d3 = genRandom(6,1)

        val roll = d1+d2+d3
        var success = "Fail"
        if(roll<=target){
            success = "Success"
        }

        val display = "Roll: $d1 + $d2 + $d3 = $roll\nTarget: $target\n$success"

        roller.text = display
    }

    private fun genRandom(max: Int, min: Int): Int {
        val r = Random()
        return r.nextInt(max - min + 1) + min
    }

    //updates points when total is changed
    inner class PointsHandler: TextWatcher{
        override fun afterTextChanged(s: Editable?) {
            if(s != null) {
                App.current.points_total = s.toString().toInt()
                setPoints()
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return pages[position]
        }

        override fun getCount(): Int {
            // Show 7 total pages.
            return 7
        }

        override fun getItemPosition(`object`: Any?): Int {
            return POSITION_NONE
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> return "Options"
                1 -> return "Description"
                2 -> return "Attributes"
                3 -> return "Reference"
                4 -> return "Skills"
                5 -> return "Weapons"
                6 -> return "Advantages"
            }
            return null
        }
    }

    //Listens for page switching, performs page updates based on which page is current
    inner class PageListener: ViewPager.OnPageChangeListener{
        override fun onPageSelected(position: Int) {
            Log.w("Position",position.toString())
            page = position
            if(position != 0) {
                PageFragment.page = PageFragment.fragments[position]
                val title = mSectionsPagerAdapter!!.getPageTitle(position).toString()
//                if(title == "Attributes"){
//                    pages[position].setFragment(title)
//                }
                pages[position].updateView(title)
            }
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageScrollStateChanged(state: Int) {
        }
    }

}
