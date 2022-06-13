package com.mambobryan.sample

import android.content.Context
import kotlin.system.exitProcess

/**
 * Jambo
 * @author Mambo Bryan
 * @email mambobryan@gmail.com
 * Created 6/13/22 at 2:45 PM
 */
class JamboExceptionHandler(val context: Context) : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(p0: Thread, p1: Throwable) {
        exit()
//        Jambo.e(p1)
    }

    private fun exit() {
//        val intent = Intent()
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//        startActivity(intent)

//        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
//        am.killBackgroundProcesses(BuildConfig.APPLICATION_ID)

//        Process.sendSignal(Process.myPid(), Process.SIGNAL_KILL);

        exitProcess(-1)

//        ActivityCompat.finishAffinity(this as Activity)

//        Runtime.getRuntime().exit(1)

//        System.exit(0)

//        System.runFinalization()

//        (context as Activity).finishAffinity()

//        val proc = System.Diagnostics.Process.GetCurrentProcess()
//        proc.Kill()

//        Application.Exit()

    }

}