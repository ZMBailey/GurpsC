package soulstudios.gurpsc

import android.widget.EditText
import android.support.v4.app.Fragment
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_attributes.view.*
import kotlinx.android.synthetic.main.fragment_description.view.*
import kotlinx.android.synthetic.main.fragment_reference.view.*

/**
 * Created by soulo_000 on 9/29/2017.
 */
class InputArray(type:String,rView:View) {
    var inputs: Array<TextView>? = null
    var fields: Array<String>? = null
    val rView = rView

    init{
        when(type){
            "Description" -> setDesc()
            "Attributes" -> setAttr()
            "Reference" -> setTables()
        }
    }

    fun getI(index: Int): TextView {
        return inputs!![index]
    }

    fun getF(index: Int): String {
        return fields!![index]
    }

    private fun setDesc() {
        inputs = arrayOf(
                rView.name_input,
                rView.title_input,
                rView.religion_input,
                rView.race_input,
                rView.gender_input,
                rView.age_input,
                rView.dob_input,
                rView.height_input,
                rView.weight_input,
                rView.size_input,
                rView.tl_input,
                rView.hair_input,
                rView.eyes_input,
                rView.skin_input,
                rView.hand_input)

        fields = arrayOf("Name",
                "Title",
                "Religion",
                "Race",
                "Gender",
                "Age",
                "Birthday",
                "Height",
                "Weight",
                "Size",
                "TL",
                "Hair",
                "Eyes",
                "Skin",
                "Hand")
    }

    private fun setTables(){
        inputs = arrayOf(
                rView.load_none,
                rView.load_light,
                rView.load_med,
                rView.load_heavy,
                rView.load_xheavy,
                rView.move_none,
                rView.move_light,
                rView.move_med,
                rView.move_heavy,
                rView.move_xheavy,
                rView.dodge_none,
                rView.dodge_light,
                rView.dodge_med,
                rView.dodge_heavy,
                rView.dodge_xheavy,
                rView.basiclift_no,
                rView.onehand_no,
                rView.twohand_no,
                rView.shove_no,
                rView.runshove_no,
                rView.carry_no,
                rView.shift_no)
    }

    private fun setAttr() {
        inputs = arrayOf(
                rView.str_input,
                rView.dex_input,
                rView.iq_input,
                rView.health_input,
                rView.will_input,
                rView.fright_input,
                rView.speed_input,
                rView.move_input,
                rView.per_input,
//        inputs.add(R.id.vision_input)
//        inputs.add(R.id.hearing_input)
//        inputs.add(R.id.taste_input)
//        inputs.add(R.id.touch_input)
                rView.basicfp_input,
                rView.tired_input,
                rView.collapse_input,
                rView.unconscious_input,
                rView.basichp_input,
                rView.reeling_input,
                rView.collapsehp_input,
                rView.check1_input,
                rView.check2_input,
                rView.check3_input,
                rView.check4_input,
                rView.dead_input
               )

        fields = arrayOf(
                "Str",
                "Dex",
                "Intel",
                "Health",
                "Will",
                "Fright",
                "Speed",
                "Move",
                "Perception",
//        fields.add("Vision")
//        fields.add("Hearing")
//        fields.add("Taste")
//        fields.add("Touch")
                "BasicFP",
                "Tired",
                "Collapse",
                "Unconscious",
                "BasicHP",
                "Reeling",
                "CollapseHP",
                "Check1",
                "Check2",
                "Check3",
                "Check4",
                "Dead")
    }
}