package soulstudios.gurpsc

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.View
import android.widget.*

/**
 * Created by soulo_000 on 10/17/2017.
 */
class Skilltable(context:Context): TableLayout(context) {
    var buttons:MutableMap<ImageView,Skill> = mutableMapOf()
    var plus:MutableMap<TextView,Int> = mutableMapOf()
    var minus:MutableMap<TextView,Int> = mutableMapOf()
    lateinit var par:PageFragment

    fun setTitle(tv:TextView){
        tv.setBackgroundColor(resources.getColor(android.R.color.black,null))
        tv.setTextColor(resources.getColor(android.R.color.white,null))
        tv.textAlignment = View.TEXT_ALIGNMENT_CENTER
    }

    fun setItem(tv:TextView){
        tv.setBackgroundColor(resources.getColor(android.R.color.white,null))
        tv.setTextColor(resources.getColor(android.R.color.black,null))
        tv.textAlignment = View.TEXT_ALIGNMENT_CENTER
    }

    fun setItemInverse(tv:TextView){
        tv.setBackgroundColor(resources.getColor(android.R.color.darker_gray,null))
        tv.setTextColor(resources.getColor(android.R.color.black,null))
        tv.textAlignment = View.TEXT_ALIGNMENT_CENTER
    }

    fun setParams(i:Int,c:Int,lp:GridLayout.LayoutParams){
        if(c < 5) {
            lp.columnSpec = GridLayout.spec(c,1f)
            lp.height = GridLayout.LayoutParams.WRAP_CONTENT
            lp.width = GridLayout.LayoutParams.WRAP_CONTENT
        }else{
            lp.height = 50
            lp.width = 50
//            lp.columnSpec = GridLayout.spec(c,1)
//            lp.columnSpec = GridLayout.spec(c,1f)
        }
        lp.rowSpec = GridLayout.spec(i,1)
    }

    inner class DeleteHandler:View.OnClickListener{
        override fun onClick(v: View?) {
            App.current.removeSkill(buttons[v]!!)
            par.setSkills()
        }
    }

    inner class PlusHandler:View.OnClickListener{
        override fun onClick(v: View?) {
            Log.w("Skill Name",App.current.skills[plus[v]!!].name)
            App.current.skills[plus[v]!!].addRank()
            Log.w("Skill Level",App.current.skills[plus[v]!!].level.toString())
            par.updateTitle()
            par.setSkills()
        }
    }

    inner class MinusHandler:View.OnClickListener{
        override fun onClick(v: View?) {
            App.current.skills[minus[v]!!].minusRank()
            par.updateTitle()
            par.setSkills()
        }
    }

    fun getEntry(lp:LinearLayout.LayoutParams):TableRow{
        val entry = TableRow(context)
        entry.orientation = LinearLayout.HORIZONTAL
        entry.layoutParams = lp
        return entry
    }

    fun skillEntry(sk:Skill,r:Int):TableRow{
        val wValues:Array<String> = arrayOf(
                sk.name,
                sk.spec!!,
                sk.diff_str,
                sk.att,
                "-",
                "+",
                (sk.rank + App.current.getAttr(sk.att).toInt()).toString()
        )

        val lp = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        val entry = TableRow(context)
        entry.layoutParams = lp
        //val entry = getEntry(lp)

        val delete = ImageView(context)
        delete.setImageDrawable(resources.getDrawable(android.R.drawable.ic_menu_delete,null))
        delete.setOnClickListener( DeleteHandler() )
        buttons.put(delete,sk)

        val fields = List(7){TextView(context)}

        val cl = List(3){TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)}
        cl[0].weight = 0.5F
        cl[1].weight = 1F
        fields[4].setOnClickListener(MinusHandler())
        fields[5].setOnClickListener(PlusHandler())

        for((i,tv) in fields.withIndex()){
            if (wValues[i] == "") {
                tv.text = "N/A"
            } else {
                tv.text = wValues[i]
            }
            if(i == 4 || i == 5){
                setTitle(tv)
                entry.addView(tv,50,50)
            }else {
                if (r.rem(2) != 0) {
                    setItemInverse(tv)
                } else {
                    setItem(tv)
                }
                entry.addView(tv,cl[1])
            }
        }
        minus.put(fields[4],r)
        plus.put(fields[5],r)
        entry.addView(delete,50,50)

        return entry
    }

    companion object {
        fun newInstance(rView:View?,page:PageFragment):Skilltable{
            val skills: MutableList<Skill> = App.current.skills
            Log.w("Skills",skills.size.toString())
            val res = page.resources
            val strings:Array<Int> = arrayOf(
                    R.string.skill_name,
                    R.string.skill_spec,
                    R.string.skill_diff,
                    R.string.skill_attr,
                    R.string.but_del,
                    R.string.but_del,
                    R.string.skill_rank,
                    R.string.but_del)
            page.resources
            val grid = Skilltable(rView!!.context)
            grid.orientation = LinearLayout.VERTICAL
            grid.par = page

            val lp = TableRow.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)

            val celp = TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)
            celp.weight = 1F

            val title = List(9){TextView(rView.context)}
            title[0].text = res.getString(R.string.skills_title)
            grid.setTitle(title[0])
            title[0].layoutParams = lp
            grid.addView( title[0], lp)

            val title_row = TableRow(rView.context)

            for(i:Int in 1..8) {
                title[i].text = res.getString(strings[i-1])
                grid.setTitle(title[i])
                if(i == 8 || i == 5 || i == 6) {
                    title[i].setTextColor(res.getColor(android.R.color.black,null))
                    title_row.addView(title[i],50,50)
                }else{
                    title_row.addView(title[i],celp)
                }
            }
            title_row.layoutParams = lp
            grid.addView(title_row,lp)

            if( skills.size > 0 && PageFragment.page == R.layout.fragment_skills) {

                val entry = MutableList(skills.size){TableRow(rView.context)}

                for ((i, sk) in skills.withIndex()) {
                    entry[i] = grid.skillEntry(sk,i)
                    grid.addView(entry[i])
                }
            }

            return grid
        }
    }
}