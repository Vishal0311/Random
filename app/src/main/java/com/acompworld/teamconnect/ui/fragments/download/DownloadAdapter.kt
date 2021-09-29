package com.acompworld.teamconnect.ui.fragments.download

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.acompworld.teamconnect.R
import java.util.*

class DownloadAdapter(var downloadList: MutableList<Any>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val HEADER_ITEM = 100
    val CARD_ITEM = 102

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        if (viewType == HEADER_ITEM) {
            val view: View = inflater.inflate(R.layout.item_download_header, parent, false)
            return HeaderViewHolder(view)
        } else if (viewType == CARD_ITEM) {
            val view: View = inflater.inflate(R.layout.item_download, parent, false)
            return CardViewHolder(view);
        } else {
            throw IllegalArgumentException("View type not supported $viewType")
        }


    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        if (item is String) {
            return HEADER_ITEM
        } else if (item is Object) {
            return CARD_ITEM
        }
        return -1
    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        if (viewType == HEADER_ITEM) {
            (holder as HeaderViewHolder).bind()
        } else if (viewType == CARD_ITEM) {
            (holder as CardViewHolder).bind()
        } else {
            Log.i("TAG", "onBindViewHolder: $viewType")
            throw IllegalArgumentException("View type not supported $viewType")
        }
    }

    override fun getItemCount() = downloadList.size




    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val category = itemView.findViewById<TextView>(R.id.category)
        fun bind() {

            val categoryVal = getItem(adapterPosition) as String?
            category.text = categoryVal
        }
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val about:TextView = itemView.findViewById(R.id.download_report_about)
        val size: TextView = itemView.findViewById(R.id.download_report_size)
        fun bind() {
            val model: DownloadModel = getItem(adapterPosition) as DownloadModel
            about.text=model.title
            size.text=model.size
        }


    }


    fun getItem(position: Int): Any {
        return downloadList[position]
    }

    fun setItem(downloadList: MutableList<Any>) {
        this.downloadList = downloadList
    }


}