package com.mambobryan.jambo

import android.util.Log
import com.mambobryan.jambo.data.JamboLog
import java.io.PrintWriter
import java.io.StringWriter

/** A facade for handling logging calls. Install instances via [`Jambo.plant()`][.plant]. */
abstract class Tree {
    @get:JvmSynthetic // Hide from public API.
    internal val explicitTag = ThreadLocal<String>()

    @get:JvmSynthetic // Hide from public API.
    internal open val tag: String?
        get() {
            val tag = explicitTag.get()
            if (tag != null) {
                explicitTag.remove()
            }
            return tag
        }

    /** Log a verbose message with optional format args. */
    open fun v(message: String?, vararg args: Any?) {
        prepareLog(Log.VERBOSE, null, message, *args)
    }

    /** Log a verbose exception and a message with optional format args. */
    open fun v(t: Throwable?, message: String?, vararg args: Any?) {
        prepareLog(Log.VERBOSE, t, message, *args)
    }

    /** Log a verbose exception. */
    open fun v(t: Throwable?) {
        prepareLog(Log.VERBOSE, t, null)
    }

    /** Log a debug message with optional format args. */
    open fun d(message: String?, vararg args: Any?) {
        prepareLog(Log.DEBUG, null, message, *args)
    }

    /** Log a debug exception and a message with optional format args. */
    open fun d(t: Throwable?, message: String?, vararg args: Any?) {
        prepareLog(Log.DEBUG, t, message, *args)
    }

    /** Log a debug exception. */
    open fun d(t: Throwable?) {
        prepareLog(Log.DEBUG, t, null)
    }

    /** Log an info message with optional format args. */
    open fun i(message: String?, vararg args: Any?) {
        prepareLog(Log.INFO, null, message, *args)
    }

    /** Log an info exception and a message with optional format args. */
    open fun i(t: Throwable?, message: String?, vararg args: Any?) {
        prepareLog(Log.INFO, t, message, *args)
    }

    /** Log an info exception. */
    open fun i(t: Throwable?) {
        prepareLog(Log.INFO, t, null)
    }

    /** Log a warning message with optional format args. */
    open fun w(message: String?, vararg args: Any?) {
        prepareLog(Log.WARN, null, message, *args)
    }

    /** Log a warning exception and a message with optional format args. */
    open fun w(t: Throwable?, message: String?, vararg args: Any?) {
        prepareLog(Log.WARN, t, message, *args)
    }

    /** Log a warning exception. */
    open fun w(t: Throwable?) {
        prepareLog(Log.WARN, t, null)
    }

    /** Log an error message with optional format args. */
    open fun e(message: String?, vararg args: Any?) {
        prepareLog(Log.ERROR, null, message, *args)
    }

    /** Log an error exception and a message with optional format args. */
    open fun e(t: Throwable?, message: String?, vararg args: Any?) {
        prepareLog(Log.ERROR, t, message, *args)
    }

    /** Log an error exception. */
    open fun e(t: Throwable?) {
        prepareLog(Log.ERROR, t, null)
    }

    /** Log an assert message with optional format args. */
    open fun wtf(message: String?, vararg args: Any?) {
        prepareLog(Log.ASSERT, null, message, *args)
    }

    /** Log an assert exception and a message with optional format args. */
    open fun wtf(t: Throwable?, message: String?, vararg args: Any?) {
        prepareLog(Log.ASSERT, t, message, *args)
    }

    /** Log an assert exception. */
    open fun wtf(t: Throwable?) {
        prepareLog(Log.ASSERT, t, null)
    }

    /** Log at `priority` a message with optional format args. */
    open fun log(priority: Int, message: String?, vararg args: Any?) {
        prepareLog(priority, null, message, *args)
    }

    /** Log at `priority` an exception and a message with optional format args. */
    open fun log(priority: Int, t: Throwable?, message: String?, vararg args: Any?) {
        prepareLog(priority, t, message, *args)
    }

    /** Log at `priority` an exception. */
    open fun log(priority: Int, t: Throwable?) {
        prepareLog(priority, t, null)
    }

    /** Return whether a message at `priority` should be logged. */
    @Deprecated("Use isLoggable(String, int)", ReplaceWith("this.isLoggable(null, priority)"))
    protected open fun isLoggable(priority: Int) = true

    /** Return whether a message at `priority` or `tag` should be logged. */
    protected open fun isLoggable(tag: String?, priority: Int) = isLoggable(priority)

    private fun prepareLog(priority: Int, t: Throwable?, message: String?, vararg args: Any?) {
        // Consume tag even when message is not loggable so that next message is correctly tagged.
        val tag = tag
        if (!isLoggable(tag, priority)) {
            return
        }

        var mMessage = message
        if (mMessage.isNullOrEmpty()) {
            if (t == null) {
                return  // Swallow message if it's null and there's no throwable.
            }
            mMessage = getStackTraceString(t)
        } else {
            if (args.isNotEmpty()) {
                mMessage = formatMessage(mMessage, args)
            }
            if (t != null) {
                mMessage += "\n" + getStackTraceString(t)
            }
        }

        log(priority, tag, mMessage, t)
    }

    /** Formats a log message with optional arguments. */
    protected open fun formatMessage(message: String, args: Array<out Any?>) =
        message.format(*args)

    fun getStackTraceString(t: Throwable): String {
        // Don't replace this with Log.getStackTraceString() - it hides
        // UnknownHostException, which is not what we want.
        val sw = StringWriter(256)
        val pw = PrintWriter(sw, false)
        t.printStackTrace(pw)
        pw.flush()
        return sw.toString()
    }

    /**
     * Write a log message to its destination. Called for all level-specific methods by default.
     *
     * @param priority Log level. See [Log] for constants.
     * @param tag Explicit or inferred tag. May be `null`.
     * @param message Formatted log message.
     * @param t Accompanying exceptions. May be `null`.
     */
    protected abstract fun log(priority: Int, tag: String?, message: String, t: Throwable?)
}