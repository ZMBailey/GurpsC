package soulstudios.gurpsc

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

class CharSheetActivity : AppCompatActivity() {

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * [FragmentPagerAdapter] derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    /**
     * The [ViewPager] that will host the section contents.
     */
    private var mViewPager: ViewPager? = null
    private var pages: Array<PageFragment> = arrayOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_char_sheet)

//        val toolbar = findViewById(R.id.toolbar) as Toolbar
//        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        var size = Point()
        windowManager.defaultDisplay.getSize(size)
        App.width = size.x
        pt_total.addTextChangedListener(PointsHandler())

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

    fun setChar(){
        val charname = "Character: ${App.current.getDesc("name")}"
        character.text = charname
    }

    fun setPoints(){
        App.current.recalcPoints()
        val points = "Unspent: ${App.current.showPoints().toString()}"
        pt_unspent.text = points
    }

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

    inner class PageListener: ViewPager.OnPageChangeListener{
        override fun onPageSelected(position: Int) {
            Log.w("Position",position.toString())
            if(position != 0) {
                PageFragment.page = PageFragment.fragments[position]
                val title = mSectionsPagerAdapter!!.getPageTitle(position).toString()
                pages[position].updateView(title)
            }
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageScrollStateChanged(state: Int) {
        }
    }

}
