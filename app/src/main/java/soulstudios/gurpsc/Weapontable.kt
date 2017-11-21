package soulstudios.gurpsc

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*

/**
 * Created by soulo_000 on 10/17/2017.
 */
class Weapontable(context: Context): LinearLayout(context) {
    var weapons:MutableMap<ImageView, Weapon> = mutableMapOf()
    lateinit var par:PageFragment

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

    //set a TextView color scheme to be an alternate line item
    fun setItemInverse(tv: TextView){
        tv.setBackgroundColor(resources.getColor(android.R.color.darker_gray,null))
        tv.setTextColor(resources.getColor(android.R.color.black,null))
        tv.textAlignment = View.TEXT_ALIGNMENT_CENTER
    }

    //set layout parameters for line items
    fun setParams(i:Int,c:Int,lp: LayoutParams){
        lp.height = LayoutParams.WRAP_CONTENT
        lp.width = LayoutParams.WRAP_CONTENT

    }

    fun findSkill(spec:String):Int{
        var rank = 0
        val skills = App.current.skills
        for(sk:Skill in skills){
            if(sk.spec == spec || sk.name == spec){
                rank = sk.rank + App.current.getAttr(sk.att).toInt()
                break
            }
        }

        return rank
    }

    fun getEntry(lp:LinearLayout.LayoutParams):LinearLayout{
        val entry = LinearLayout(context)
        entry.orientation = LinearLayout.VERTICAL
        entry.background = resources.getDrawable(R.drawable.outer_border,null)
        entry.layoutParams = lp
        return entry
    }

    fun meleeEntry(mll:Melee):LinearLayout{
        val strings:Array<String> = arrayOf(
                resources.getString(R.string.skill_rank),
                resources.getString(R.string.w_dmg),
                resources.getString(R.string.w_reach),
                resources.getString(R.string.w_parry)
        )
        val rank = findSkill(mll.w_class).toString()
        val wValues:Array<String> = arrayOf(
                "  $rank",
                "  ${mll.dmg}",
                "  ${mll.reach}",
                "  ${mll.parry}"
        )

        val lp = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)
        val entry = getEntry(lp)

        val row = List(2){LinearLayout(context)}
        row[1].orientation = LinearLayout.HORIZONTAL
        row[1].layoutParams = lp
        row[1].orientation = LinearLayout.HORIZONTAL
        row[1].layoutParams = lp
        row[1].setPadding(10,0,0,10)

        val name = TextView(context)
        name.text = mll.name
        name.layoutParams = lp
        setTitle(name)
        row[0].addView( name)

        val title = List(4){ TextView(context) }

//        set up and add all the column titles
        for(i:Int in 0..3) {
            title[i].text = strings[i]
            setItem(title[i])
        }

        val delete = ImageView(context)
        delete.setImageDrawable(resources.getDrawable(android.R.drawable.ic_menu_delete,null))
        delete.setOnClickListener( DeleteHandler() )
        weapons.put(delete,mll)

        val fields = List(4){TextView(context)}

        for((i,tv) in fields.withIndex()){
            tv.text = wValues[i]
        }

        val cl = List(3){LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)}
        cl[0].weight = 0.5F
        cl[1].weight = 1F

        for((i,f) in fields.withIndex()){
            row[1].addView(title[i])
            row[1].addView(f, cl[1])
        }
        row[1].addView(delete,50,50)
        entry.addView(row[0])
        entry.addView(row[1])

        return entry
    }

    fun rangedEntry(rgd:Ranged):LinearLayout{
        val strings:Array<String> = arrayOf(
                resources.getString(R.string.skill_rank),
                resources.getString(R.string.w_dmg),
                resources.getString(R.string.w_range),
                resources.getString(R.string.title_acc),
                resources.getString(R.string.w_rof),
                resources.getString(R.string.w_shots),
                resources.getString(R.string.w_rcl)
        )
        val rank = findSkill(rgd.w_class)
        val wValues:Array<String> = arrayOf(
                "  $rank",
                "  ${rgd.dmg}",
                "  ${rgd.range}",
                "  ${rgd.acc} ",
                "  ${rgd.rof}",
                "  ${rgd.shots}",
                "  ${rgd.rcl}"
        )

        val lp = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)
        val entry = getEntry(lp)

        val row = List(3){LinearLayout(context)}
        for((i,ll) in row.withIndex()){
            ll.orientation = LinearLayout.HORIZONTAL
            ll.layoutParams = lp
            if(i>0){ll.setPadding(10,0,0,0)}
        }

        val name = TextView(context)
        name.text = rgd.name
        name.layoutParams = lp
        setTitle(name)
        row[0].addView( name)

        val title = List(7){ TextView(context) }

//        set up and add all the column titles
        for(i:Int in 0..6) {
            title[i].text = strings[i]
            setItem(title[i])
        }

        val delete = ImageView(context)
        delete.setImageDrawable(resources.getDrawable(android.R.drawable.ic_menu_delete,null))
        delete.setOnClickListener( DeleteHandler() )
        weapons.put(delete,rgd)

        val fields = List(7){TextView(context)}

        for((i,tv) in fields.withIndex()){
            tv.text = wValues[i]
        }

        val cl = List(3){LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)}
        cl[0].weight = 0.5F
        cl[1].weight = 1F

        for((i,f) in fields.withIndex()){
            when {
                (i<3) -> {
                    row[1].addView(title[i])
                    row[1].addView(f, cl[1])
                }
                else -> {
                    row[2].addView(title[i])
                    row[2].addView(f, cl[1])
                }
            }
        }
        row[2].addView(delete,50,50)
        entry.addView(row[0])
        entry.addView(row[1])
        entry.addView(row[2])

        return entry
    }

    inner class DeleteHandler: OnClickListener {
        override fun onClick(v: View?) {
            val dialog = AlertDialog.Builder(context).create()
            var name:String =  ""
            if(weapons[v] is Melee){
                val w:Melee = weapons[v] as Melee
                name = w.name}
            else{val w:Ranged = weapons[v] as Ranged
                name = w.name}

            val dh = DoneHandler(weapons[v]!!)
            dialog.setMessage("Remove $name ?")
            dialog.setButton(-1, "Delete", dh)
            dialog.setButton(-2, "Cancel", dh)
            dialog.show()
        }
    }

    //delete weapon
    inner class DoneHandler(w:Weapon) : DialogInterface.OnClickListener {
        val weapon = w
        override fun onClick(d: DialogInterface, i: Int) {
            if (i == -1) {
                App.current.removeWeapon(weapon)
                par.setWeapons()
            } else if (i == -2) {
                d.dismiss()
            }
        }
    }

    companion object {
        //create a new table of Melee weapons
        fun newMelee(rView: View?, name: String,page: PageFragment): Weapontable {
            val melee: MutableList<Melee> = App.current.melee
            Log.w("Melee", melee.size.toString())

            val grid = Weapontable(rView!!.context)
            grid.orientation = LinearLayout.VERTICAL
            grid.par = page

            val lp = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)

            val title = TextView(rView.context)
            title.text = name
            grid.setTitle(title)
            title.layoutParams = lp
            grid.addView( title, lp)


            if( melee.size > 0 && PageFragment.page == R.layout.fragment_weapons) {

                val entries = MutableList(melee.size){ LinearLayout(rView.context) }

                val ll = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)

                Log.w("Melee",melee.size.toString())
                for ((i, mll) in melee.withIndex()) {
                    entries[i] = grid.meleeEntry(mll)
                    grid.addView( entries[i],ll)
                }
            }

            return grid
        }

        fun newRanged(rView: View?, name:String, page:PageFragment): Weapontable {
            val ranged: MutableList<Ranged> = App.current.ranged
            Log.w("Ranged", ranged.size.toString())

            val grid = Weapontable(rView!!.context)
            grid.orientation = LinearLayout.VERTICAL
            grid.par = page

            val lp = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)

            val title = TextView(rView.context)
            title.text = name
            grid.setTitle(title)
            title.layoutParams = lp
            grid.addView( title, lp)


            if( ranged.size > 0 && PageFragment.page == R.layout.fragment_weapons) {

                val entries = MutableList(ranged.size){ LinearLayout(rView.context) }

                val ll = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)

                Log.w("Ranged",ranged.size.toString())
                for ((i, rgd) in ranged.withIndex()) {
                    entries[i] = grid.rangedEntry(rgd)
                    grid.addView( entries[i],ll)
                }
            }

            return grid
        }
    }
}