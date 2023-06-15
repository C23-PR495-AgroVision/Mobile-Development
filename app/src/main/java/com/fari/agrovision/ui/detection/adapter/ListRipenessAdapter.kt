package com.fari.agrovision.ui.detection.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fari.agrovision.data.remote.model.detection.Ripeness
import com.fari.agrovision.databinding.ItemRipenessBinding

class ListRipenessAdapter(private val listRipeness: List<Ripeness>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemRipenessBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RipenessViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RipenessViewHolder -> holder.bind(listRipeness[position])
        }
    }

    override fun getItemCount(): Int = listRipeness.size


    inner class RipenessViewHolder(private val binding: ItemRipenessBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(ripeness: Ripeness) {
            val title = ripeness.title
            val imageUrl = ripeness.imgUrl
            binding.tvFruitName.text = title
            Glide.with(binding.root)
                .load(imageUrl)
                .into(binding.ivFruit)
            binding.root.setOnClickListener {
                onItemClickCallback.onItemRipenessClicked(ripeness)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemRipenessClicked(item: Ripeness)
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}