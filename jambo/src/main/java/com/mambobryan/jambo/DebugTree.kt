package com.mambobryan.jambo

import android.app.Application
import android.os.Build
import android.util.Log
import com.mambobryan.jambo.data.JamboLog
import com.mambobryan.jambo.data.LogType
import com.mambobryan.jambo.ui.JamboViewModel
import java.util.regex.Pattern

/** A [Tree] for debug builds. Automatically infers the tag from the calling class. */
class DebugTree(val application: Application) : Tree(), Thread.UncaughtExceptionHandler {

   private val viewModel = JamboViewModel(application)
   private val oldHandler = Thread.getDefaultUncaughtExceptionHandler()

    init {
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    private val fqcnIgnore = listOf(
        Jambo::class.java.name,
        Tree::class.java.name,
        DebugTree::class.java.name,
        Jambo.Forest::class.java.name
    )

    override val tag: String?
        get() = super.tag ?: Throwable().stackTrace
            .first { it.className !in fqcnIgnore }
            .let(::createStackElementTag)

    /**
     * Extract the tag which should be used for the message from the `element`. By default
     * this will use the class name without any anonymous class suffixes (e.g., `Foo$1`
     * becomes `Foo`).
     *
     * Note: This will not be called if a [manual tag][.tag] was specified.
     */
    protected open fun createStackElementTag(element: StackTraceElement): String? {
        var tag = element.className.substringAfterLast('.')
        val m = ANONYMOUS_CLASS.matcher(tag)
        if (m.find()) {
            tag = m.replaceAll("")
        }
        // Tag length limit was removed in API 26.
        return if (tag.length <= MAX_TAG_LENGTH || Build.VERSION.SDK_INT >= 26) {
            tag
        } else {
            tag.substring(0, MAX_TAG_LENGTH)
        }
    }

    /**
     * Break up `message` into maximum-length chunks (if needed) and send to either
     * [Log.println()][Log.println] or
     * [Log.wtf()][Log.wtf] for logging.
     *
     * {@inheritDoc}
     */
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (message.length < MAX_LOG_LENGTH) {
            print(priority, tag, message)
            return
        }

        // Split by line, then ensure each line can fit into Log's maximum length.
        var i = 0
        val length = message.length
        while (i < length) {
            var newline = message.indexOf('\n', i)
            newline = if (newline != -1) newline else length
            do {
                val end = Math.min(newline, i + MAX_LOG_LENGTH)
                val part = message.substring(i, end)
                print(priority, tag, part)
                i = end
            } while (i < newline)
            i++
        }
    }

    private fun print(priority: Int, tag: String?, message: String) {
        if (priority == Log.ASSERT) {
            Log.wtf(tag, message)
        } else {
            Log.println(priority, tag, message)
        }.also {

            val type = when (priority) {
                Log.ASSERT -> LogType.ASSERT
                Log.DEBUG -> LogType.DEBUG
                Log.WARN -> LogType.WARN
                Log.INFO -> LogType.INFO
                Log.VERBOSE -> LogType.VERBOSE
                Log.ERROR -> LogType.ERROR
                else -> LogType.ALL
            }

            saveLog(
                JamboLog(
                    tag = tag.toString(),
                    packageName = application.packageName,
                    message = message,
                    type = type
                )
            )
        }
    }

    private fun saveLog(log: JamboLog) {
        viewModel.saveLog(log)
    }

    companion object {
        private const val MAX_LOG_LENGTH = 4000
        private const val MAX_TAG_LENGTH = 23
        private val ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$")
    }

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        saveLog(
            JamboLog(
                tag = Exception(throwable).localizedMessage,
                packageName = application.packageName,
                message = getStackTraceString(throwable),
                type = LogType.ERROR
            )
        )
        oldHandler?.uncaughtException(thread, throwable)
    }

}