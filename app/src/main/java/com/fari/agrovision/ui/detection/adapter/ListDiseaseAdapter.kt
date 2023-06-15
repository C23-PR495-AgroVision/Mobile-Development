package com.fari.agrovision.ui.detection.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fari.agrovision.data.remote.model.detection.PlantDisease
import com.fari.agrovision.databinding.ItemDiseaseBinding

class ListDiseaseAdapter(private val listDisease: List<PlantDisease>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemDiseaseBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return DiseaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DiseaseViewHolder -> holder.bind(listDisease[position])
        }
    }

    override fun getItemCount(): Int = listDisease.size


    inner class DiseaseViewHolder(private val binding: ItemDiseaseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(disease: PlantDisease) {
            val title = disease.title
            val imageUrl = disease.imgUrl
            binding.tvPlantName.text = title
            Glide.with(binding.root)
                .load(imageUrl)
                .into(binding.ivPlant)
            binding.root.setOnClickListener {
                onItemClickCallback.onItemDiseaseClicked(disease)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemDiseaseClicked(item: PlantDisease)
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}