package soulstudios.gurpsc

import android.app.Application
import android.graphics.Point
import io.objectbox.BoxStore

/**
 * Created by soulo_000 on 10/7/2017.
 */
class App : Application() {

    override fun onCreate(){
        super.onCreate()
        boxStore = MyObjectBox.builder().androidContext(this).build()
    }

    companion object {

        lateinit var boxStore: BoxStore
        var current: GCharacter = GCharacter()
        var width:Int = 0
    }
}