package com.mambobryan.jambo.helpers

import android.view.View
import android.view.WindowManager
import androidx.appcompat.widget.SearchView
import com.mambobryan.jambo.data.JamboLog
import com.mambobryan.jambo.data.LogType

/**
 * Jambo
 * @author Mambo Bryan
 * @email mambobryan@gmail.com
 * Created 6/10/22 at 3:55 PM
 */
fun View.setupFullHeight() {
    val layoutParams = this.layoutParams
    layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
    this.layoutParams = layoutParams
}

inline fun SearchView.onQueryTextChanged(crossinline listener: (String) -> Unit) {
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            listener(newText.orEmpty())
            return true
        }
    })
}

fun JamboLog.getColor(): Pair<String, String> {
    return when (this.type) {
        LogType.ALL -> Pair("#F5F5F5", "#C2C2C2")
        LogType.INFO -> Pair("#EBF9FF", "#85DAFF")      // blue
        LogType.VERBOSE -> Pair("#EBF9FF", "#85DAFF")   // blue
        LogType.ERROR -> Pair("#FFEBEB", "#FF8585")     // red
        LogType.DEBUG -> Pair("#FFFFEB", "#FFFF85")     // yellow
        LogType.WARN -> Pair("#FFF8EB", "#FFD485")      // orange
        LogType.ASSERT -> Pair("#EBFFEB", "#85FF85")    // green
    }
}

fun JamboLog.getFullTag(): String {
    val s = when (this.type) {
        LogType.ALL -> "Any"
        LogType.INFO -> "I"
        LogType.VERBOSE -> "V"
        LogType.ERROR -> "E"
        LogType.DEBUG -> "D"
        LogType.WARN -> "W"
        LogType.ASSERT -> "A"
    }
    return s.plus("/").plus(this.tag)
}