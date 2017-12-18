package soulstudios.gurpsc

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

/**
 * Created by soulo_000 on 10/13/2017.
 */
@Entity
class Advantage(){
    @Id
    var id:Long = 0
    var name:String = "Name"
    var cost:Int = 0
    var hasLevels:Boolean = false
    var levelCost:Int = 0
    var levels:Int = 0
    var desc:String = "$1"

    //lateinit var player: ToOne<GCharacter>

    constructor( n:String,c:Int,hl:Boolean,cl:Int,l:Int,desc:String):this(){
        this.name = n
        hasLevels = hl
        if(hasLevels){
            this.levelCost = cl
            this.levels = l
            this.findCost()
        }
        else{this.cost = c}
        this.desc = desc
    }

    fun addLevel(){
        levels++
        findCost()
    }

    fun minusLevel(){
        levels--
        findCost()
    }

    fun findCost(){
        if(hasLevels){
            cost = levels*levelCost
        }
    }
}