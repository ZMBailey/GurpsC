package soulstudios.gurpsc

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

/**
 * Created by soulo_000 on 10/13/2017.
 */
@Entity
class Ranged():Weapon(){
    @Id
    var id:Long = 0
    var w_class:String = "sword"
    var name:String = "Weapon Name"
    var tl:String = "0"
    var dmg:String = "none"
    var cost:String = "$1"
    var weight:String = "0"
    var st:String = "-"
    //-------------------Ranged---------->
    var acc:String = "1"
    var range:String = ""
    var rof:String = "1"
    var shots:String = "1(20)"
    var bulk:String = "-1"
    var rcl:String = "2"
    var lc:String ="4"

    //lateinit var player: ToOne<GCharacter>

    constructor( n:String, wc:String,dm:String,a:String, rn:String, rf:String,
                 sh:String,rl:String):this(){
        this.name = n
        this.w_class = wc
        //this.tl = tl
        this.dmg = dm
        //this.cost = ct
//        this.weight = wg
//        this.st = str

        this.acc = a
        this.range = rn
        this.rof = rf
        this.shots = sh
//        this.bulk = blk
        this.rcl = rl
//        this.lc = l

        //this.player.target = g
    }


}