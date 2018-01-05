package soulstudios.gurpsc

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

/**
 * Created by soulo_000 on 10/17/2017.
 */
class Advantagetable(context: Context): LinearLayout(context) {
    var deleteButtons:MutableMap<ImageView, Advantage> = mutableMapOf()
    var plus:MutableMap<TextView,Int> = mutableMapOf()
    var minus:MutableMap<TextView,Int> = mutableMapOf()
    lateinit var par: PageFragment

    //set a TextView color scheme to be a Title
    fun setTitle(tv: TextView){
        tv.setBackgroundColor(resources.getColor(android.R.color.black,null))
        tv.setTextColor(resources.getColor(android.R.color.white,null))
        tv.textAlignment = View.TEXT_ALIGNMENT_CENTER
    }

    //set a TextView color scheme to be a normal line item
    fun setItem(tv: TextView){
        tv.setTextColor(resources.getColor(android.R.color.black,null))
        tv.textAlignment = View.TEXT_ALIGNMENT_CENTER
    }

    fun findCost(adv: Advantage):Int{
        val per = adv.levelCost
        val lvl = adv.levels

        return per*lvl
    }

    inner class PlusHandler:View.OnClickListener{
        override fun onClick(v: View?) {
            Log.w("Adv Name",App.current.advantages[plus[v]!!].name)
            App.current.advantages[plus[v]!!].addLevel()
            Log.w("Adv Level",App.current.advantages[plus[v]!!].levels.toString())
            par.updateTitle()
            par.setAdvs()
        }
    }

    inner class MinusHandler:View.OnClickListener{
        override fun onClick(v: View?) {
            App.current.advantages[minus[v]!!].minusLevel()
            par.updateTitle()
            par.setAdvs()
        }
    }

    fun getEntry(lp: LayoutParams): LinearLayout {
        val entry = LinearLayout(context)
        entry.orientation = VERTICAL
        entry.background = resources.getDrawable(R.drawable.outer_border,null)
        entry.layoutParams = lp
        return entry
    }

    fun staticEntry(adv: Advantage): LinearLayout {

        val lp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        val entry = getEntry(lp)

        val row = List(2){ LinearLayout(context) }
        row[1].orientation = HORIZONTAL
        row[1].layoutParams = lp
        row[1].orientation = HORIZONTAL
        row[1].layoutParams = lp
        row[1].setPadding(10,0,0,10)

        val name = TextView(context)
        name.text = adv.name
        name.layoutParams = lp
        setTitle(name)
        row[0].addView( name)

        val delete = ImageView(context)
        delete.setImageDrawable(resources.getDrawable(android.R.drawable.ic_menu_delete,null))
        delete.setOnClickListener( DeleteHandler() )
        deleteButtons.put(delete,adv)

        val desc = TextView(context)
        desc.setElegantTextHeight(true)
        desc.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
        desc.setSingleLine(false)
        desc.text = adv.desc

        val cl = List(3){ LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) }
        cl[0].weight = 0.5F
        cl[1].weight = 1F

        row[1].addView(desc, cl[1])

        row[1].addView(delete,50,50)
        entry.addView(row[0])
        entry.addView(row[1])

        return entry
    }

    fun levelEntry(adv: Advantage,r: Int): LinearLayout {
        val strings:Array<String> = arrayOf(
                resources.getString(R.string.a_lvls),
                resources.getString(R.string.a_cost),
                "-",
                "+"
        )
        val cost = findCost(adv)
        val aValues:Array<String> = arrayOf(
                "  ${adv.levels}",
                "  $cost",
                adv.desc
        )

        val lp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        val entry = getEntry(lp)

        val row = List(3){ LinearLayout(context) }
        row[1].orientation = HORIZONTAL
        row[1].layoutParams = lp
        row[1].orientation = HORIZONTAL
        row[1].layoutParams = lp
        row[1].setPadding(10,0,0,10)

        val name = TextView(context)
        name.text = adv.name
        name.layoutParams = lp
        setTitle(name)
        row[0].addView( name)

        val delete = ImageView(context)
        delete.setImageDrawable(resources.getDrawable(android.R.drawable.ic_menu_delete,null))
        delete.setOnClickListener( DeleteHandler() )
        deleteButtons.put(delete,adv)

        val desc = TextView(context)
        desc.setElegantTextHeight(true)
        desc.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
        desc.setSingleLine(false)
        desc.text = aValues[2]

        val lvl_titles = List(4){TextView(context)}

        for((i,tv) in lvl_titles.withIndex()){
            tv.text = strings[i]
            if(i<2) {
                setItem(tv)
            }else{
                setTitle(tv)
            }
        }

        val lvl_tv = TextView(context)
        val cost_tv = TextView(context)
        lvl_tv.text = aValues[0]
        cost_tv.text = aValues[1]

        val cl = List(3){ LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) }
        cl[0].weight = 0.5F
        cl[1].weight = 1F
        lvl_titles[2].setOnClickListener(MinusHandler())
        lvl_titles[3].setOnClickListener(PlusHandler())
        minus.put(lvl_titles[2],r)
        plus.put(lvl_titles[3],r)

        row[1].addView(desc, cl[1])
        row[1].addView(delete,50,50)
        row[2].addView(lvl_titles[2],50,50)
        row[2].addView(lvl_titles[3],50,50)
        row[2].addView(lvl_titles[0], cl[1])
        row[2].addView(lvl_tv,cl[1])
        row[2].addView(lvl_titles[1], cl[1])
        row[2].addView(cost_tv,cl[1])
        entry.addView(row[0])
        entry.addView(row[1])
        entry.addView(row[2])

        return entry
    }

    inner class DeleteHandler: OnClickListener {
        override fun onClick(v: View?) {
            val dialog = AlertDialog.Builder(context).create()
            val name:String = deleteButtons[v]!!.name
            val dh = DoneHandler(deleteButtons[v]!!)
            dialog.setMessage("Remove $name ?")
            dialog.setButton(-1, "Delete", dh)
            dialog.setButton(-2, "Cancel", dh)
            dialog.show()
        }
    }

    //delete weapon
    inner class DoneHandler(a: Advantage) : DialogInterface.OnClickListener {
        val adv = a
        override fun onClick(d: DialogInterface, i: Int) {
            if (i == -1) {
                App.current.removeAdv(adv)
                par.setAdvs()
            } else if (i == -2) {
                d.dismiss()
            }
        }
    }

    companion object {
        //create a new table of Advantages
        fun newInstance(rView: View?, name: String, page: PageFragment): Advantagetable {
            val advantage: MutableList<Advantage> = App.current.advantages
            Log.w("Advantages", advantage.size.toString())

            val grid = Advantagetable(rView!!.context)
            grid.orientation = VERTICAL
            grid.par = page

            val lp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

            val title = TextView(rView.context)
            title.text = name
            grid.setTitle(title)
            title.layoutParams = lp
            grid.addView( title, lp)


            if( advantage.size > 0 && PageFragment.page == R.layout.fragment_advantages) {

                val entries = MutableList(advantage.size){ LinearLayout(rView.context) }

                val ll = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

                Log.w("Advantage", advantage.size.toString())
                for ((i, adv) in advantage.withIndex()) {
                    if(adv.hasLevels){
                        entries[i] = grid.levelEntry(adv,i)
                    }else {
                        entries[i] = grid.staticEntry(adv)
                    }
                    grid.addView( entries[i],ll)
                }
            }

            return grid
        }
    }
}