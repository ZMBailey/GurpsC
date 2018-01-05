package soulstudios.gurpsc

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

/**
 * Created by soulo_000 on 10/13/2017.
 */
@Entity
class Melee():Weapon(){
    @Id
    var id:Long = 0
    var w_class:String = "sword"
    var name:String = "Weapon Name"
    var tl:String = "0"
    var dmg:String = "none"
    var cost:String = "$1"
    var weight:String = "0"
    var st:String = "-"
    //-------------------Melee----------->
    var reach:String = ""
    var parry = "No"
    var lc:String ="4"

    //lateinit var player: ToOne<GCharacter>

    constructor(n:String,wc:String,dm:String,rh:String,pa:String):this(){
        this.name = n
        this.w_class = wc
        this.dmg = dm
        this.reach = rh
        this.parry = pa

    }


}