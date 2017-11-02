package soulstudios.gurpsc

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.View
import android.widget.*

/**
 * Created by soulo_000 on 10/17/2017.
 */
class Skilltable(context:Context): GridLayout(context) {
    var buttons:MutableMap<ImageView,Skill> = mutableMapOf()
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
                    R.string.skill_rank,
                    R.string.but_del)
            page.resources
            val grid = Skilltable(rView!!.context)
            grid.par = page
            grid.rowCount = skills.size+3
            grid.columnCount = 6

            val lp: GridLayout.LayoutParams = GridLayout.LayoutParams()
            lp.columnSpec = GridLayout.spec(0,6)
            lp.rowSpec = GridLayout.spec(0,1)
            lp.width = LayoutParams.MATCH_PARENT
            lp.height = GridLayout.LayoutParams.WRAP_CONTENT

            var celp: GridLayout.LayoutParams = GridLayout.LayoutParams()
            celp.height = GridLayout.LayoutParams.WRAP_CONTENT
            celp.width = GridLayout.LayoutParams.WRAP_CONTENT
            celp.rowSpec = GridLayout.spec(1,1)

            val title = List(7){TextView(rView.context)}
            title[0].text = res.getString(R.string.skills_title)
            grid.setTitle(title[0])
            title[0].layoutParams = lp
            grid.addView( title[0], lp)

            for(i:Int in 1..6) {
                title[i].text = res.getString(strings[i-1])
                grid.setTitle(title[i])
                celp =  GridLayout.LayoutParams()
                celp.rowSpec = GridLayout.spec(1, 1)
                celp.columnSpec = GridLayout.spec(i - 1, 1)
                celp.columnSpec = GridLayout.spec(i - 1, 1f)
                if(i < 6) {
                    celp.height = GridLayout.LayoutParams.WRAP_CONTENT
                    celp.width = GridLayout.LayoutParams.WRAP_CONTENT
                }else{
                    celp.height = 50
                    celp.width = 50
                    title[i].setTextColor(res.getColor(android.R.color.black,null))
                }
                grid.addView(title[i], celp)
            }


            if( skills.size > 0 && PageFragment.page == R.layout.fragment_skills) {

                val name = List(skills.size){TextView(rView.context)}
                val spec = List(skills.size){TextView(rView.context)}
                val diff = List(skills.size){TextView(rView.context)}
                val attr = List(skills.size){TextView(rView.context)}
                val rank = List(skills.size){TextView(rView.context)}
                val delete = List(skills.size){ImageView(rView.context)}
                val dh = grid.DeleteHandler()

                val sp = MutableList(6){GridLayout.LayoutParams()}

                for ((i, sk) in skills.withIndex()) {
                    for (j: Int in 0..5) {
                        sp[j] = GridLayout.LayoutParams()
                        grid.setParams(i + 2, j, sp[j])
                    }
                    name[i].text = sk.name
                    if (sk.spec == null || sk.spec == "") {
                        spec[i].text = "N/A"
                    } else {
                        spec[i].text = sk.spec
                    }
                    diff[i].text = sk.diff_str
                    attr[i].text = sk.att
                    rank[i].text = (sk.rank + App.current.getAttr(sk.att).toInt()).toString()

                    if (i.rem(2) != 0) {
                        grid.setItemInverse(name[i])
                        grid.setItemInverse(spec[i])
                        grid.setItemInverse(diff[i])
                        grid.setItemInverse(attr[i])
                        grid.setItemInverse(rank[i])
                    } else {
                        grid.setItem(name[i])
                        grid.setItem(spec[i])
                        grid.setItem(diff[i])
                        grid.setItem(attr[i])
                        grid.setItem(rank[i])
                    }

//                    delete[i].text = res.getText(R.string.but_del)
                    delete[i].setImageDrawable(res.getDrawable(android.R.drawable.ic_menu_delete,null))
//                    grid.setItemInverse(delete[i])
//                    delete[i].setBackgroundColor(res.getColor(android.R.color.holo_red_dark,null))
                    delete[i].setPadding(2,0,50,0)
                    delete[i].setOnClickListener( dh )
                    grid.buttons.put(delete[i],sk)
                    Log.w("Skill", i.toString())
                    Log.w("Skill", sk.name)
                    grid.addView(name[i], sp[0])
                    grid.addView(spec[i], sp[1])
                    grid.addView(diff[i], sp[2])
                    grid.addView(attr[i], sp[3])
                    grid.addView(rank[i], sp[4])
                    grid.addView( delete[i], sp[5])
                }
            }

            return grid
        }
    }
}