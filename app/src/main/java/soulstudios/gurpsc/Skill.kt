package soulstudios.gurpsc

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

/**
 * Created by soulo_000 on 10/13/2017.
 */
@Entity
class Skill(){

    @Id
    var id:Long = 0
    @Transient private val difficulty:HashMap<String,Int> = hashMapOf(
            "Ea" to -1,
            "Av" to -2,
            "Hd" to -3,
            "Vh" to -4
    )
    var name:String = "Skill Name"
    var att:String = "Str"
    var spec:String? = null
    var notes:String = ""
    var diff:Int = difficulty["Ea"]!!
    var diff_str = "Ea"
    var level:Int = 1
    var rank:Int = 0
    var cost:Int = 1

    init{

    }

    constructor(n:String,a:String,s:String?,note:String,d:String,l:Int):this(){
        this.name = n
        this.att = a
        this.spec = s
        this.notes = note
        this.diff = difficulty[d]!!
        this.diff_str = d
        this.level = l

        rank = diff+level

        cost = pointCost()
    }

    fun changeRank(){
        rank = diff+level
    }

    fun pointCost():Int{
        when{
            level == 1 -> cost = 1
            level == 2 -> cost = 2
            level>3 -> cost = (level-1)*4
        }
        return cost
    }
}