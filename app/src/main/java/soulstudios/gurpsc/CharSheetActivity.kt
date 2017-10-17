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

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        var size = Point()
        windowManager.defaultDisplay.getSize(size)
        App.width = size.x

        pages = arrayOf(
                PageFragment.newInstance(1,window),
                PageFragment.newInstance(2,window),
                PageFragment.newInstance(3,window),
                PageFragment.newInstance(4,window),
                PageFragment.newInstance(5,window)
        )
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container) as ViewPager
        mViewPager!!.adapter = mSectionsPagerAdapter
        mViewPager!!.addOnPageChangeListener(PageListener())
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


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return pages[position]
        }

        override fun getCount(): Int {
            // Show 4 total pages.
            return 5
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> return "Options"
                1 -> return "Description"
                2 -> return "Attributes"
                3 -> return "Reference"
                4 -> return "Skills"
            }
            return null
        }
    }

    inner class PageListener(): ViewPager.OnPageChangeListener{
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

    /**
     * A placeholder fragment containing a simple view.
     */
//    class PlaceholderFragment : Fragment() {
//
//        private var page: Int = 0
//        private var rView: View? = null
//
//        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
//                                  savedInstanceState: Bundle?): View? {
//            val rootView = inflater!!.inflate(R.layout.fragment_c, container, false)
//            rView = rootView
//
//            when(page){
//                R.layout.fragment_description -> setHandlers("Description", rootView)
//                R.layout.fragment_attributes -> setHandlers("Attributes", rootView)
//
//            }
//            return rootView
//        }
//
//        private fun setHandlers(type: String, rootView: View){
//            val inputs = soulstudios.gurpsc.InputArray(type)
//            val fields = arrayOfNulls<EditText>(inputs.inputs.size)
//            for (i in 0..(inputs.inputs.size-1)){
//                fields[i] = rootView.findViewById(inputs.getI(i)) as EditText
//                fields[i]?.addTextChangedListener(TextHandler(fields[i],rootView))
//            }
//        }
//
//        fun setP(p: Int){
//            page = p
//        }
//
//        internal fun setAttributes(rootView: View, curr: String) {
//            val inputs = soulstudios.gurpsc.InputArray("Attributes")
//            val field = arrayOfNulls<TextView>(inputs.inputs.size)
//            for (i in 0..(inputs.inputs.size-1)) {
//                field[i] = rootView.findViewById(inputs.getI(i)) as TextView
//                if (field[i]?.tag.toString() != curr) {
//                    field[i]?.text = current.getAttr(inputs.getF(i))
//                }
//            }
//
//        }
//
//        inner class TextHandler(p: EditText?,r: View): TextWatcher
//        {
//            private val textbox: EditText? = p
//            private val name: String = textbox?.tag.toString()
//            private val rView: View = r
//
//            override fun afterTextChanged(s: Editable?) {
//                if(name == "Desc"){
//                    current?.setDesc(s.toString(),name)
//                }else{
//                    if((s.toString() != current?.getAttr(name)) && s.toString() != ""){
//                        current?.set(name)?.execute(Integer.parseInt(s.toString()),0,0)
//                        setAttributes(rView,name)
//                    }
//                }
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            }
//        }
//
//        companion object {
//            /**
//             * The fragment argument representing the section number for this
//             * fragment.
//             */
//            private val ARG_SECTION_NUMBER = "section_number"
//
//            /**
//             * Returns a new instance of this fragment for the given section
//             * number.
//             */
//            fun newInstance(sectionNumber: Int): PlaceholderFragment {
//                val fragment = PlaceholderFragment()
//                val args = Bundle()
//                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
//                fragment.arguments = args
//                fragment.setP(sectionNumber-1)
//                return fragment
//            }
//
//        }
//    }


}
