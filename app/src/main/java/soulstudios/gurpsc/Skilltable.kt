package soulstudios.gurpsc

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.GridView
import android.widget.TextView

/**
 * Created by soulo_000 on 10/17/2017.
 */
class Skilltable(context:Context): GridLayout(context) {
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
        lp.height = GridLayout.LayoutParams.WRAP_CONTENT
        lp.width = GridLayout.LayoutParams.WRAP_CONTENT
        lp.rowSpec = GridLayout.spec(i,1)
        lp.columnSpec = GridLayout.spec(c,1f)
    }

    companion object {
        fun newInstance(rView:View?,res: Resources):Skilltable{
            val skills: MutableList<Skill> = App.current.getSkills()
            Log.w("Skills",skills.size.toString())

            val strings:Array<Int> = arrayOf(
                    R.string.skill_name,
                    R.string.skill_spec,
                    R.string.skill_diff,
                    R.string.skill_attr,
                    R.string.skill_rank)

            val grid = Skilltable(rView!!.context)
            grid.rowCount = skills.size+3
            grid.columnCount = 5

            val lp: GridLayout.LayoutParams = GridLayout.LayoutParams()
            lp.columnSpec = GridLayout.spec(0,5)
            lp.rowSpec = GridLayout.spec(0,1)
            lp.width = App.width
            lp.height = GridLayout.LayoutParams.WRAP_CONTENT

            var celp: GridLayout.LayoutParams = GridLayout.LayoutParams()
            celp.height = GridLayout.LayoutParams.WRAP_CONTENT
            celp.width = GridLayout.LayoutParams.WRAP_CONTENT
            celp.rowSpec = GridLayout.spec(1,1)

            val title: Array<TextView> =Array<TextView>(6,{ i -> TextView(rView!!.context) })
            title[0].text = res.getString(R.string.skills_title)
            grid.setTitle(title[0])
            title[0].layoutParams = lp
            grid.addView( title[0], lp)

            for(i:Int in 1..5) {
                title[i].text = res.getString(strings[i-1])
                grid.setTitle(title[i])
                celp =  GridLayout.LayoutParams()
                celp.height = GridLayout.LayoutParams.WRAP_CONTENT
                celp.width = GridLayout.LayoutParams.WRAP_CONTENT
                celp.rowSpec = GridLayout.spec(1,1)
                celp.columnSpec = GridLayout.spec(i-1,1)
                celp.columnSpec = GridLayout.spec(i-1, 1f)
                grid.addView(title[i], celp)
            }


            if( skills.size > 0 && PageFragment.page == R.layout.fragment_skills) {

                val name = Array<TextView>(skills.size, { i -> TextView(rView!!.context) })
                val spec = Array<TextView>(skills.size, { i -> TextView(rView!!.context) })
                val diff = Array<TextView>(skills.size, { i -> TextView(rView!!.context) })
                val attr = Array<TextView>(skills.size, { i -> TextView(rView!!.context) })
                val rank = Array<TextView>(skills.size, { i -> TextView(rView!!.context) })
                //val dh = DeleteHandler()

                val sp = Array<GridLayout.LayoutParams>(5, { i -> GridLayout.LayoutParams() })

                for ((i, sk) in skills.withIndex()) {
                    for (j: Int in 0..4) {
                        sp[j] = GridLayout.LayoutParams()
                        grid.setParams(i + 2, j, sp[j])
                    }
                    name[i].text = sk.name
                    if (sk.spec == null) {
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

//                delete[i].text = resources.getText(R.string.but_delete)
//                delete[i].setTextColor(resources.getColor(android.R.color.black,null))
//                delete[i].background = resources.getDrawable(R.drawable.rounded_purple,null)
//                delete[i].setOnClickListener( dh )
//                buttons.put(delete[i],gc.id)
                    Log.w("Skill", i.toString())
                    Log.w("Skill", sk.name)
                    grid.addView(name[i], sp[0])
                    grid.addView(spec[i], sp[1])
                    grid.addView(diff[i], sp[2])
                    grid.addView(attr[i], sp[3])
                    grid.addView(rank[i], sp[4])
//                grid.addView( delete[i], (buttonWidth*.35).toInt(),
//                        GridLayout.LayoutParams.WRAP_CONTENT )
                }
            }

            return grid
        }


    }
}