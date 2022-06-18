package com.tabasumu.jambo

import android.app.Application
import org.jetbrains.annotations.NonNls
import java.util.*
import java.util.Collections.unmodifiableList

/**
 * Jambo
 * @author Mambo Bryan
 * @email mambobryan@gmail.com
 * Created 6/9/22 at 9:38 PM
 */
class Jambo private constructor(builder: Builder) {
    private val application: Application = builder.application
    private val showNotifications: Boolean = builder.showNotifications

    init {
        ///throw AssertionError()
        plant(DebugTree(application, showNotifications))
    }

    /**
     * Builder
     */
    class Builder(val application: Application) {
        internal var showNotifications: Boolean = false

        fun enableNotifications(enable: Boolean) = apply {
            this.showNotifications = enable
        }

        fun build() {
            Jambo(this)
        }

    }

    companion object Forest : Tree() {
        /** Log a verbose message with optional format args. */
        @JvmStatic
        override fun v(@NonNls message: String?, vararg args: Any?) {
            tree.forEach { it.v(message, *args) }
        }

        /** Log a verbose exception and a message with optional format args. */
        @JvmStatic
        override fun v(t: Throwable?, @NonNls message: String?, vararg args: Any?) {
            tree.forEach { it.v(t, message, *args) }
        }

        /** Log a verbose exception. */
        @JvmStatic
        override fun v(t: Throwable?) {
            tree.forEach { it.v(t) }
        }

        /** Log a debug message with optional format args. */
        @JvmStatic
        override fun d(@NonNls message: String?, vararg args: Any?) {
            tree.forEach { it.d(message, *args) }
        }

        /** Log a debug exception and a message with optional format args. */
        @JvmStatic
        override fun d(t: Throwable?, @NonNls message: String?, vararg args: Any?) {
            tree.forEach { it.d(t, message, *args) }
        }

        /** Log a debug exception. */
        @JvmStatic
        override fun d(t: Throwable?) {
            tree.forEach { it.d(t) }
        }

        /** Log an info message with optional format args. */
        @JvmStatic
        override fun i(@NonNls message: String?, vararg args: Any?) {
            tree.forEach { it.i(message, *args) }
        }

        /** Log an info exception and a message with optional format args. */
        @JvmStatic
        override fun i(t: Throwable?, @NonNls message: String?, vararg args: Any?) {
            tree.forEach { it.i(t, message, *args) }
        }

        /** Log an info exception. */
        @JvmStatic
        override fun i(t: Throwable?) {
            tree.forEach { it.i(t) }
        }

        /** Log a warning message with optional format args. */
        @JvmStatic
        override fun w(@NonNls message: String?, vararg args: Any?) {
            tree.forEach { it.w(message, *args) }
        }

        /** Log a warning exception and a message with optional format args. */
        @JvmStatic
        override fun w(t: Throwable?, @NonNls message: String?, vararg args: Any?) {
            tree.forEach { it.w(t, message, *args) }
        }

        /** Log a warning exception. */
        @JvmStatic
        override fun w(t: Throwable?) {
            tree.forEach { it.w(t) }
        }

        /** Log an error message with optional format args. */
        @JvmStatic
        override fun e(@NonNls message: String?, vararg args: Any?) {
            tree.forEach { it.e(message, *args) }
        }

        /** Log an error exception and a message with optional format args. */
        @JvmStatic
        override fun e(t: Throwable?, @NonNls message: String?, vararg args: Any?) {
            tree.forEach { it.e(t, message, *args) }
        }

        /** Log an error exception. */
        @JvmStatic
        override fun e(t: Throwable?) {
            tree.forEach { it.e(t) }
        }

        /** Log an assert message with optional format args. */
        @JvmStatic
        override fun wtf(@NonNls message: String?, vararg args: Any?) {
            tree.forEach { it.wtf(message, *args) }
        }

        /** Log an assert exception and a message with optional format args. */
        @JvmStatic
        override fun wtf(t: Throwable?, @NonNls message: String?, vararg args: Any?) {
            tree.forEach { it.wtf(t, message, *args) }
        }

        /** Log an assert exception. */
        @JvmStatic
        override fun wtf(t: Throwable?) {
            tree.forEach { it.wtf(t) }
        }

        /** Log at `priority` a message with optional format args. */
        @JvmStatic
        override fun log(priority: Int, @NonNls message: String?, vararg args: Any?) {
            tree.forEach { it.log(priority, message, *args) }
        }

        /** Log at `priority` an exception and a message with optional format args. */
        @JvmStatic
        override fun log(
            priority: Int,
            t: Throwable?,
            @NonNls message: String?,
            vararg args: Any?
        ) {
            tree.forEach { it.log(priority, t, message, *args) }
        }

        /** Log at `priority` an exception. */
        @JvmStatic
        override fun log(priority: Int, t: Throwable?) {
            tree.forEach { it.log(priority, t) }
        }

        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            throw AssertionError() // Missing override for log method.
        }

        /**
         * A view into Jambo's planted trees as a tree itself. This can be used for injecting a logger
         * instance rather than using static methods or to facilitate testing.
         */
        @Suppress(
            "NOTHING_TO_INLINE", // Kotlin users should reference `Tree.Forest` directly.
            "NON_FINAL_MEMBER_IN_OBJECT" // For japicmp check.
        )
        @JvmStatic
        open inline fun asTree(): Tree = this

        /** Set a one-time tag for use on the next logging call. */
        @JvmStatic
        fun tag(tag: String): Tree {
            for (tree in tree) {
                tree.explicitTag.set(tag)
            }
            return this
        }

        /** Add a new logging tree. */
        @JvmStatic
        private fun plant(tree: Tree) {
            require(tree !== this) { "Cannot plant Jambo into itself." }
            synchronized(trees) {
                trees.add(tree)
                this.tree = trees.toTypedArray()
            }
        }

        /** Adds new logging trees. */
        @JvmStatic
        private fun plant(vararg trees: Tree) {
            for (tree in trees) {
                requireNotNull(tree) { "trees contained null" }
                require(tree !== this) { "Cannot plant Jambo into itself." }
            }
            synchronized(Forest.trees) {
                Collections.addAll(Forest.trees, *trees)
                tree = Forest.trees.toTypedArray()
            }
        }

        /** Remove a planted tree. */
        @JvmStatic
        private fun uproot(tree: Tree) {
            synchronized(trees) {
                require(trees.remove(tree)) { "Cannot uproot tree which is not planted: $tree" }
                this.tree = trees.toTypedArray()
            }
        }

        /** Remove all planted trees. */
        @JvmStatic
        private fun uprootAll() {
            synchronized(trees) {
                trees.clear()
                tree = emptyArray()
            }
        }

        /** Return a copy of all planted [trees][Tree]. */
        @JvmStatic
        fun forest(): List<Tree> {
            synchronized(trees) {
                return unmodifiableList(trees.toList())
            }
        }

        @get:[JvmStatic JvmName("treeCount")]
        private val treeCount
            get() = tree.size

        // Both fields guarded by 'trees'.
        private val trees = ArrayList<Tree>()

        @Volatile
        private var tree = emptyArray<Tree>()
    }


}