package com.acompworld.teamconnect.ui.fragments.lms

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.acompworld.teamconnect.R
import com.acompworld.teamconnect.api.model.responses.LMSResponseData
import com.acompworld.teamconnect.api.model.responses.LMSResponseResource
import com.acompworld.teamconnect.databinding.FragmentLmsBinding
import com.acompworld.teamconnect.databinding.ItemLmsBinding
import com.acompworld.teamconnect.utils.load

class LMSAdapter (
    private val viewModel: LMSViewModel,
    private val lms: List<LMSResponseData>
        ) : RecyclerView.Adapter<LMSAdapter.LMSViewHolder>() {

    private val viewPool = RecyclerView.RecycledViewPool()

    override fun getItemCount(): Int  = lms.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LMSViewHolder  {
        /*var root = LayoutInflater.from(parent.context).inflate(R.layout.lms_item,parent,false);
        return LMSViewHolder(root)*/
        /*val inflater = LayoutInflater.from(parent.context)

        val binding = ItemLmsBinding.inflate(inflater, parent, false)
        return LMSViewHolder(binding)*/
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentLmsBinding.inflate(inflater, parent, false)
        return LMSViewHolder(binding)
    }

    override fun onBindViewHolder(holderLMS: LMSViewHolder, position: Int) {
        //holder.bind(lms.get(position))
        val data = lms.get(position)
        holderLMS.recyclerView.adapter = LMSChildAdapter(data.resources!!)
        //holderLMS.recyclerView.recycledViewPool = viewPool

        //val resources = data.resources;
        //resources.forEach { holder.bind(it) }
    }
    inner class LMSViewHolder(
        private val binding : FragmentLmsBinding
        ) : RecyclerView.ViewHolder(binding.root){
        val recyclerView: RecyclerView = binding.rvLms
        //val textView:TextView = itemView.textView
    }

    /*inner class LMSViewHolder(
        private val binding: ItemLmsBinding
    ) :RecyclerView.ViewHolder(binding.root) {
        fun bind(data:LMSResponseResource) {
            val iconBookmark = if(data.isBookmarked) R.drawable.ic_bookmarked else
                R.drawable.ic_bookmark

            binding.tvTitle.text = data.title
            binding.tvFiletype.text = data.filetype
            binding.ivPhoto.load(data.photoPath)
            binding.ivBookmark.setImageResource(iconBookmark)
        }
    }*/
}

class LMSChildAdapter(private val resources: List<LMSResponseResource>)
    : RecyclerView.Adapter<LMSChildAdapter.ResourceViewHolder>() {

    override fun getItemCount(): Int = resources.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = ItemLmsBinding.inflate(inflater, parent, false)
        return ResourceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResourceViewHolder, position: Int) {
        val data = resources[position]
        holder.bind(data)
    }

        inner class ResourceViewHolder(
            private val binding: ItemLmsBinding
        ) :RecyclerView.ViewHolder(binding.root) {
            fun bind(data:LMSResponseResource) {
                val iconBookmark = if(data.isBookmarked) R.drawable.ic_bookmarked else
                    R.drawable.ic_bookmark

                binding.tvTitle.text = data.title
                binding.tvFiletype.text = data.filetype
                binding.ivPhoto.load(data.photoPath)
                binding.ivBookmark.setImageResource(iconBookmark)
            }
        }
}