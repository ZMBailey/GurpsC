package soulstudios.gurpsc

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var mTextMessage: TextView? = null

//    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
//        when (item.itemId) {
//            R.id.navigation_home -> {
//                mTextMessage!!.setText(R.string.title_home)
//                return@OnNavigationItemSelectedListener true
//            }
//            R.id.navigation_dashboard -> {
//                mTextMessage!!.setText(R.string.title_dashboard)
//                return@OnNavigationItemSelectedListener true
//            }
//            R.id.navigation_notifications -> {
//                mTextMessage!!.setText(R.string.title_notifications)
//                return@OnNavigationItemSelectedListener true
//            }
//            R.id.navigation_reference -> {
//                mTextMessage!!.setText(R.string.title_notifications)
//                return@OnNavigationItemSelectedListener true
//            }
//            R.id.navigation_skills -> {
//                mTextMessage!!.setText(R.string.title_notifications)
//                return@OnNavigationItemSelectedListener true
//            }
//        }
//        false
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        mTextMessage = findViewById(R.id.message) as TextView
////        val navigation = findViewById(R.id.navigation) as BottomNavigationView
////        navigation.isInEditMode()
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

//    inner class SheetAdapter(ctxt:Context): FragmentPagerAdapter() {
//        lateinit var ctxt: Context
//
//        init{
//            this.ctxt = ctxt
//        }
//
//        override fun getCount(): Int {
//            return 7
//        }
//
//        override fun getItem(position: Int): Fragment {
//            return
//        }
//    }
}
