package com.cybera1.spyonist.Logs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cybera1.spyonist.databinding.ItemAccessLogsBinding
import com.cybera1.spyonist.toDate
import com.cybera1.spyonist.toTime

class AccessLogsAdapter: RecyclerView.Adapter<AccessLogsAdapter.AccessLogsViewHolder>() {

    inner class AccessLogsViewHolder(val binding: ItemAccessLogsBinding): RecyclerView.ViewHolder(binding.root)

    private val differCallback = object :DiffUtil.ItemCallback<AccessLog>() {
        override fun areItemsTheSame(oldItem: AccessLog, newItem: AccessLog): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AccessLog, newItem: AccessLog): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccessLogsViewHolder {
        val binding = ItemAccessLogsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        )
        return AccessLogsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AccessLogsViewHolder, position: Int) {
        val log = differ.currentList[position]
        holder.binding.apply {
            tvTimeStamp.text = log.time.toTime()
            tvDate.text = log.time.toDate()
            tvAppId.text = log.appId
            tvAppName.text = log.appName
            ivIndicator.setImageResource(log.indicatorType.drawable)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}