package com.tabasumu.sample

import android.app.Application
import com.tabasumu.jambo.Jambo


/**
 * Jambo
 * @author Mambo Bryan
 * @email mambobryan@gmail.com
 * Created 6/9/22 at 9:45 PM
 */
class App : Application() {


    override fun onCreate() {
        super.onCreate()

        plantJambo()

    }

    private fun plantJambo() {

        Jambo.Builder(this)
            .enableNotifications(true)
            .build()

    }

}