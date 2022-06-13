package com.mambobryan.jambo.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mambobryan.jambo.data.JamboLog
import com.mambobryan.jambo.data.LogType
import com.mambobryan.jambo.databinding.ItemJamboLogBinding

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

                tvLogTag.text = log.tag
                tvLogPackage.text = log.packageName
                tvLogMessage.text = log.message
                tvLogType.text = log.type.name

                val color = when (log.type) {
                    LogType.ALL -> "#FFFEEB"
                    LogType.INFO -> "#EBEBEB"
                    LogType.VERBOSE -> "#C2F3FF"
                    LogType.ERROR -> "#FFC2C2"
                    LogType.DEBUG -> "#FFE1C2"
                    LogType.WARN -> "#C2FFC2"
                    LogType.ASSERT -> "#D2C3FE"
                }
//                val color = poem.topic?.color ?: "#94F292"
                layoutLogBg.setBackgroundColor(Color.parseColor(color))

            }
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