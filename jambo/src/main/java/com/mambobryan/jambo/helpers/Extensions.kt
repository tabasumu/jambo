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
        LogType.INFO -> Pair("#D6FFE6", "#008F37")      // green
        LogType.VERBOSE -> Pair("#EBEBEB", "#666666")   // gray
        LogType.ERROR -> Pair("#FFEBEE", "#E60026")     // red
        LogType.DEBUG -> Pair("#EEFAFC", "#26A7BA")     // blue
        LogType.WARN -> Pair("#FFEFD6", "#F58F00")      // orange
        LogType.ASSERT -> Pair("#EBE2F3", "#8147AE")    // purple
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