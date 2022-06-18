package com.tabasumu.jambo.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tabasumu.jambo.data.JamboLog
import com.tabasumu.jambo.data.LogType
import com.tabasumu.jambo.databinding.ItemJamboLogBinding
import com.tabasumu.jambo.helpers.getColor
import com.tabasumu.jambo.helpers.getFullTag
import java.text.SimpleDateFormat
import java.util.*

/**
 * Jambo
 * @author Mambo Bryan
 * @email mambobryan@gmail.com
 * Created 6/10/22 at 2:35 PM
 */
class JamboLogAdapter :
    PagingDataAdapter<JamboLog, JamboLogAdapter.JamboLogViewHolder>(JAMBO_LOG_COMPARATOR) {

    companion object {
        val JAMBO_LOG_COMPARATOR = object : DiffUtil.ItemCallback<JamboLog>() {
            override fun areItemsTheSame(oldItem: JamboLog, newItem: JamboLog): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: JamboLog, newItem: JamboLog): Boolean {
                return oldItem == newItem
            }
        }
    }

    private lateinit var onLogClickListener: OnJamboLogClickListener

    fun setListener(listener: OnJamboLogClickListener) {
        onLogClickListener = listener
    }

    inner class JamboLogViewHolder(private val binding: ItemJamboLogBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {

                layoutLogClick.setOnClickListener {
                    if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                        val log = getItem(absoluteAdapterPosition)
                        if (log != null)
                            onLogClickListener.onLogClicked(log)
                    }
                }

            }
        }

        fun bind(log: JamboLog) {
            binding.apply {

                val color = log.getColor()
                val tag = log.getFullTag()

                tvLogDate.text = log.timestamp.toLogDateString()
                tvLogMessage.text = log.message
                tvLogTag.text = tag
                layoutLogBg.setBackgroundColor(Color.parseColor(color.first))
                layoutLogHintColor.setBackgroundColor(Color.parseColor(color.second))

            }
        }

        private fun color(log: JamboLog): Pair<String, String> {
            val color = when (log.type) {
                LogType.ALL -> Pair("#F5F5F5", "#C2C2C2")
                LogType.INFO -> Pair("#EBF9FF", "#85DAFF")      // blue
                LogType.VERBOSE -> Pair("#EBF9FF", "#85DAFF")   // blue
                LogType.ERROR -> Pair("#FFEBEB", "#FF8585")     // red
                LogType.DEBUG -> Pair("#FFFFEB", "#FFFF85")     // yellow
                LogType.WARN -> Pair("#FFF8EB", "#FFD485")      // orange
                LogType.ASSERT -> Pair("#EBFFEB", "#85FF85")    // green
            }
            return color
        }

        private fun Long.toLogDateString(): String {
            val date = Date(this)
            val format = SimpleDateFormat("dd.MM.yyyy   HH:mm:ss")
            return format.format(date)
        }

    }

    override fun onBindViewHolder(holder: JamboLogViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null)
            holder.bind(currentItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JamboLogViewHolder {
        val binding = ItemJamboLogBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return JamboLogViewHolder(binding)
    }

    interface OnJamboLogClickListener {
        fun onLogClicked(log: JamboLog)
    }

}