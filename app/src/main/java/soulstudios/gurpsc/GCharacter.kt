package soulstudios.gurpsc

import android.util.Log
import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

/**
 * Created by soulo_000 on 9/29/2017.
 */

class GCharacter {
    var id: Long = 0
    private var points: Int = 0
    //----
    //    private String name;
    //    private String title;
    //    private String religion;
    //----
    private var description: MutableMap<String,String> = hashMapOf(
            "Name" to "",
            "Title" to "",
            "Religion" to "",
            "Age" to "",
            "Birthday" to "",
            "Height" to "",
            "Weight" to "",
            "Size" to "",
            "TL" to "",
            "Hair" to "",
            "Eyes" to "",
            "Skin" to "",
            "Hand" to "")
    //----
    var attributes: MutableMap<String, Int> = hashMapOf(
            "Str" to 10,
            "Dex" to 10,
            "IQ" to 10,
            "Health" to 10,
            "Speed" to 5,
            "Move" to 5,
            "Will" to 10,
            "Per" to 10,
            "Tired" to 3,
            "Collapse" to 0,
            "Unconscious" to -10,
            "BasicFP" to 10,
            "BasicHP" to 10,
            "Reeling" to 3,
            "CollapseHP" to 0,
            "Check1" to -10,
            "Check2" to -20,
            "Check3" to -30,
            "Check4" to -40,
            "Dead" to -50,
            "Lift0" to 20,
            "Lift1" to 40,
            "Lift2" to 60,
            "Lift3" to 120,
            "Lift4" to 200,
            "Move0" to 5,
            "Move1" to 4,
            "Move2" to 3,
            "Move3" to 2,
            "Move4" to 1,
            "Dodge0" to 8,
            "Dodge1" to 7,
            "Dodge2" to 6,
            "Dodge3" to 5,
            "Dodge4" to 4,
            "LiftBasic" to 20,
            "LiftOne" to 40,
            "LiftTwo" to 160,
            "LiftShove" to 240,
            "LiftRun" to 480,
            "LiftCarry" to 300,
            "LiftShift" to 1000)
    //----
    private var setters: Map<String, Update> = hashMapOf(
            "Str" to SetStr(),
            "Dex" to SetDex(),
            "IQ" to SetIQ(),
            "Health" to SetHealth(),
            "Will" to SetWill(),
            "Speed" to SetSpeed(),
            "Move" to SetMove(),
            "BasicHP" to SetHP(),
            "HPStatus" to SetHPStatus(),
            "BasicFP" to SetFatigue(),
            "FStatus" to SetFStatus(),
            "Per" to SetPer(),
            "Refs" to SetRefs(),
            "Lift" to SetLift(),
            "LiftMove" to SetLiftMove())
    //----
    var name: Map<Int,String> = hashMapOf(
            R.id.name_input to "Name",
            R.id.title_input to "Title",
            R.id.religion_input to "Religion",
            R.id.race_input to "Race",
            R.id.gender_input to "Gender",
            R.id.age_input to "Age",
            R.id.dob_input to "Birthday",
            R.id.height_input to "Height",
            R.id.weight_input to "Weight",
            R.id.size_input to "Size",
            R.id.tl_input to "TL",
            R.id.hair_input to "Hair",
            R.id.eyes_input to "Eyes",
            R.id.skin_input to "Skin",
            R.id.hand_input to "Hand",
            R.id.str_input to "Str",
            R.id.dex_input to "Dex",
            R.id.iq_input to "IQ",
            R.id.health_input to "Health",
            R.id.will_input to "Will",
            R.id.fright_input to "Fright",
            R.id.speed_input to "Speed",
            R.id.move_input to "Move",
            R.id.per_input to "Per",
//        inputs.add(R.id.vision_input)
//        inputs.add(R.id.hearing_input)
//        inputs.add(R.id.taste_input)
//        inputs.add(R.id.touch_input)
            R.id.basicfp_input to "BasicFP",
            R.id.tired_input to "Tired",
            R.id.collapse_input to "Collapse",
            R.id.unconscious_input to "Unconscious",
            R.id.basichp_input to "BasicHP",
            R.id.reeling_input to "Reeling",
            R.id.collapsehp_input to "CollapseHP",
            R.id.check1_input to "Check1",
            R.id.check2_input to "Check2",
            R.id.check3_input to "Check3",
            R.id.check4_input to "Check4",
            R.id.dead_input to "Dead",
            R.id.load_none to "Lift0",
            R.id.load_light to "Lift1",
            R.id.load_med to "Lift2",
            R.id.load_heavy to "Lift3",
            R.id.load_xheavy to "Lift4",
            R.id.move_none to "Move0",
            R.id.move_light to "Move1",
            R.id.move_med to "Move2",
            R.id.move_heavy to "Move3",
            R.id.move_xheavy to "Move4",
            R.id.dodge_none to "Dodge0",
            R.id.dodge_light to "Dodge1",
            R.id.dodge_med to "Dodge2",
            R.id.dodge_heavy to "Dodge3",
            R.id.dodge_xheavy to "Dodge4",
            R.id.basiclift_no to "LiftBasic",
            R.id.onehand_no to "LiftOne",
            R.id.twohand_no to "LiftTwo",
            R.id.shove_no to "LiftShove",
            R.id.runshove_no to "LiftRun",
            R.id.carry_no to "LiftCarry",
            R.id.shift_no to "LiftShift"
    )
    private var skills:MutableList<Skill> = mutableListOf(
            Skill("Lockpicking","Dex",null,"None","Av",2),
            Skill("Hacking","IQ",null,"None","Av",3)
    )
    private var speed = 5f
    //----

    init{
        points = 100
        //setFunctions()
    }

    /*setById allows the Set function to be called using an Id number
    * of a field instead of the name.*/
    fun setById(field: Int,input: Number,stat1: Int,stat2: Int){
        set(name[field]!!,input,stat1,stat2)
    }

    /*Set sets the Input into the specified Field.
    * field: the name field to be changed, as a String.
    * input: the new number assigned to the field, a generic Number
    * so it can be either Int or Float(Speed uses decimals).
    * stat1: the previous value of the first stat the field is based on.
    * stat2: the previous value of the second stat the number is based on.*/
    fun set(field: String,input: Number,stat1: Int,stat2: Int){
        if (setters.containsKey(field)) {
            if(input is Int) {
                setters[field]?.execute(input,stat1,stat2)
            }else if(input is Float){
                setters[field]?.execute(input,stat1,stat2)
            }
        }
    }

    fun setDesc(input: String, field: Int) {
        description.put(name[field]!!, input)
    }

    fun setDescById(input: String, field: String){
        description.put(field,input)
    }

    /*External getters for attribute and description HashMaps
    * accepts an Int containing the ID number of a field, translates
    * that into a field name, and uses the name to index into the HashMap.
    * if the HashMap does not contain the field, it returns 0 or blank.*/
    fun getDescById(field: Int): String? {
        return if (description.containsKey(name[field])) {
            description[name[field]]
        } else {
            ""
        }
    }

    fun getDesc(field: String): String? {
        return if (description.containsKey(field)) {
            description[field]
        } else {
            ""
        }
    }

    fun getAttrById(field: Int): String {
        val attr = name[field]!!
        return getAttr(attr)
    }

    fun getAttr(field: String): String {
        val lift: Array<String> = arrayOf(
                "Lift0",
                "Lift1",
                "Lift2",
                "Lift3",
                "Lift4",
                "LiftB",
                "LiftOne",
                "LiftTwo",
                "LiftShove",
                "LiftRun",
                "LiftCarry",
                "LiftShift"
        )
        return when{
            field == "Speed" -> { java.lang.Float.toString(speed) }
            lift.contains(field) -> { attributes[field].toString() + " lb" }
            attributes.containsKey(field) -> { attributes[field].toString() }
            else -> { "0" }
        }
    }

    fun getSkills():MutableList<Skill>{
        return skills
    }

    fun addSkill(name:String,attr:String,spec:String?,note:String,diff:String,lvl:Int){
        val newskill = Skill(name,attr,spec,note,diff,lvl)
        skills.add(newskill)
    }

    fun removeSkill(s:Skill){
        skills.remove(s)
    }

    fun export(): GChar{
        val d = description.values.joinToString{e -> e}
        val a = attributes.values.joinToString(",")
        Log.w("D",d)
        Log.w("A",a)
        return GChar(id,description["Name"]!!,d,a,points,speed,skills)
    }

    fun load(new: GChar){
        var i = 0
        points = new.points
        id = new.id
        skills = new.skills
        val d:List<String> = new.getD()
        val a:List<Int> = new.getA()
        for(f:String in description.keys){
            description.put(f,d[i])
            i++
        }
        i=0
        for(f:String in attributes.keys){
            Log.w(f,a[i].toString())
            if(f == "Speed"){
                speed = new.speed
            }else {
                attributes[f] = a[i]
            }
            i++
        }
        Log.w("Attributes",attributes.toString())
    }

    /*Update interface: implemented by each field that needs to
    * update based on input*/
    interface Update {
        fun execute(input: Int, stat1: Int, stat2: Int)
        fun execute(input: Float, stat1: Int, stat2: Int)
    }

    //--------set main Attributes---------------------------------------->
    private inner class SetStr : Update {
        override fun execute(input: Int, stat1: Int, stat2: Int) {
            if(input>2) {
                val str = attributes["Str"]!!
                Log.w("Str: ", str.toString())
                Log.w("New Str: ", input.toString())
                points += (str - input) * 10

                attributes.put("Str", input)
                set("BasicHP", attributes["BasicHP"]!!, str, 0)
                set("Lift", input, 0, 0)
                set("LiftMove", input, 0, 0)
            }
        }

        override fun execute(input: Float, stat1: Int, stat2: Int) {}
    }

    private inner class SetDex : Update {
        override fun execute(input: Int, stat1: Int, stat2: Int) {
            if(input>2) {
                val dex = attributes["Dex"]!!

                points += (dex - input) * 20

                attributes.put("Dex", input)
                set("Speed", speed, dex, attributes["Health"]!!)
            }
        }

        override fun execute(input: Float, stat1: Int, stat2: Int) {}
    }

    private inner class SetHealth : Update {
        override fun execute(input: Int, stat1: Int, stat2: Int) {
            if(input>2) {
                val health = attributes["Health"]!!

                points += (health - input) * 10

                attributes.put("Health", input)
                set("BasicFP", attributes["BasicFP"]!!, health, 0)
                set("Speed", speed, attributes["Dex"]!!, health)
            }
        }

        override fun execute(input: Float, stat1: Int, stat2: Int) {}
    }

    private inner class SetIQ : Update {
        override fun execute(input: Int, stat1: Int, stat2: Int) {
            if(input>2) {
                val iq = attributes["IQ"]!!

                points += (iq - input) * 20

                attributes.put("IQ", input)
                set("Will", attributes["Will"]!!, iq, 0)
                set("Per", attributes["Per"]!!, iq, 0)
            }
        }

        override fun execute(input: Float, stat1: Int, stat2: Int) {}
    }

    //--------Set Secondary------------------------------------------------->
    private inner class SetWill : Update {
        override fun execute(input: Int, stat1: Int, stat2: Int) {
            val iq = attributes["IQ"]!!
            var st1 = stat1
            if(stat1 == 0){
                st1 = iq
            }

            var will = attributes["Will"]!! - st1
            val new = input - st1

            points += (will - new) * 5

            will = new + iq
            attributes.put("Will", will)
        }

        override fun execute(input: Float, stat1: Int, stat2: Int) {}
    }

    private inner class SetSpeed : Update {
        override fun execute(input: Int, stat1: Int, stat2: Int) {}

        override fun execute(input: Float, stat1: Int, stat2: Int) {
            val dex = attributes["Dex"]!!.toFloat()
            val health = attributes["Health"]!!.toFloat()
            var st1 = stat1.toFloat()
            var st2 = stat2.toFloat()
            if(stat1 == 0){
                st1 = dex
            }
            if(stat2 == 0){
                st2 = health
            }
            val sum = st1 + st2

            val spd = speed - sum / 4
            val new = input - sum / 4

            points += ((spd - new) * 20).toInt()

            speed = new + (dex + health) / 4
            set("Move",speed.toInt(), 0, 0)
        }
    }

    private inner class SetMove : Update {
        override fun execute(input: Int, stat1: Int, stat2: Int) {
            val move = attributes["Move"]!!

            points += (move - input) * 5

            attributes.put("Move", input)
            set("Refs",input,0,0)
        }

        override fun execute(input: Float, stat1: Int, stat2: Int) {}
    }

    private inner class SetRefs : Update {
        override fun execute(input: Int, stat1: Int, stat2: Int) {
            attributes.put("Move0", input)
            attributes.put("Move1", (input*0.8).toInt())
            attributes.put("Move2", (input*0.6).toInt())
            attributes.put("Move3", (input*0.4).toInt())
            attributes.put("Move4", (input*0.2).toInt())

            val bd = input+3
            attributes.put("Dodge0", bd)
            attributes.put("Dodge1", bd-1)
            attributes.put("Dodge2", bd-2)
            attributes.put("Dodge3", bd-3)
            attributes.put("Dodge4", bd-4)
        }

        override fun execute(input: Float, stat1: Int, stat2: Int) {}
    }

    private inner class SetHP : Update {
        override fun execute(input: Int, stat1: Int, stat2: Int) {
            val str = attributes["Str"]!!
            var st1 = stat1
            if(stat1 == 0){
                st1 = str
            }
            Log.w("Str in HP", str.toString())
            Log.w("Stat1 in HP", stat1.toString())
            var hp = attributes["BasicHP"]!! - st1
            val new = input - st1
            Log.w("hp", hp.toString())
            points += (hp - new) * 2

            hp = new + str
            Log.w("new", new.toString())
            Log.w("new hp", hp.toString())
            attributes.put("BasicHP", hp)
            set("HPStatus",hp, 0, 0)
        }

        override fun execute(input: Float, stat1: Int, stat2: Int) {}
    }

    private inner class SetHPStatus : Update {
        override fun execute(input: Int, stat1: Int, stat2: Int) {
            attributes.put("Reeling", (input - 1) / 3)
            attributes.put("Check1", -input)
            attributes.put("Check2", -2 * input)
            attributes.put("Check3", -3 * input)
            attributes.put("Check4", -4 * input)
            attributes.put("Dead", -5 * input)
        }

        override fun execute(input: Float, stat1: Int, stat2: Int) {}
    }

    private inner class SetFatigue : Update {
        override fun execute(input: Int, stat1: Int, stat2: Int) {
            val health = attributes["Health"]!!
            var st1 = stat1
            if(stat1 == 0){
                st1 = health
            }
            var fatigue = attributes["BasicFP"]!! - st1
            val new = input - st1

            points += (fatigue - new) * 3

            fatigue = new + health
            attributes.put("BasicFP", fatigue)
            set("FStatus",fatigue, 0, 0)
        }

        override fun execute(input: Float, stat1: Int, stat2: Int) {}
    }

    private inner class SetFStatus : Update {
        override fun execute(input: Int, stat1: Int, stat2: Int) {
            attributes.put("Tired", (input - 1) / 3)
            attributes.put("Unconscious", -input)
        }

        override fun execute(input: Float, stat1: Int, stat2: Int) {}
    }

    private inner class SetPer : Update {
        override fun execute(input: Int, stat1: Int, stat2: Int) {
            val iq = attributes["IQ"]!!
            var st1 = stat1
            if(stat1 == 0){
                st1 = iq
            }
            var per = attributes["Per"]!! - st1
            val new = input - st1

            points += (per - new) * 5

            per = new + iq
            attributes.put("Per", per)
        }

        override fun execute(input: Float, stat1: Int, stat2: Int) {}
    }

    private inner class SetLift : Update {
        override fun execute(input: Int, stat1: Int, stat2: Int) {
            val bl = input*input/5
            attributes.put("Lift0", bl)
            attributes.put("Lift1", bl*2)
            attributes.put("Lift2", bl*3)
            attributes.put("Lift3", bl*6)
            attributes.put("Lift4", bl*10)
        }

        override fun execute(input: Float, stat1: Int, stat2: Int) {}
    }

    private inner class SetLiftMove : Update {
        override fun execute(input: Int, stat1: Int, stat2: Int) {
            val bl = input*input/5
            attributes.put("LiftBasic", bl)
            attributes.put("LiftOne", bl*2)
            attributes.put("LiftTwo", bl*8)
            attributes.put("LiftShove", bl*12)
            attributes.put("LiftRun", (bl*12)*2)
            attributes.put("LiftCarry", bl*15)
            attributes.put("LiftShift", bl*50)
        }

        override fun execute(input: Float, stat1: Int, stat2: Int) {}
    }
}