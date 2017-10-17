package soulstudios.gurpsc

import android.util.Log
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany

/**
 * Created by soulo_000 on 10/7/2017.
 */
@Entity
class GChar() {
    @Id var id:Long = 0
    var name = ""
    var points: Int = 0
    var speed: Float = 0f
    var desc: String = ""
    var attr: String = ""
    lateinit var skills: ToMany<Skill>

    constructor(i: Long,n: String, d: String, a: String, p: Int, s: Float,sk:MutableList<Skill>):this(){
        this.id = i
        this.name = n
        this.points = p
        this.speed = s
        this.desc = d
        this.attr = a
        sk.toCollection(this.skills)
    }

    fun getD():List<String>{
       val rg = ","
        return desc.split(rg).toList()
    }

    fun getA():List<Int>{
        val rg = ","
        val li = attr.split(rg).toList()
        val out: MutableList<Int> = mutableListOf()
        Log.w("Attr",attr)
        for(index in li.indices){
            out.add(Integer.parseInt(li[index]))
        }
        return out
    }
}