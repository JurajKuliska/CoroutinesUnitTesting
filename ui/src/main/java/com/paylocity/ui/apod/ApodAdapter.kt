package com.paylocity.ui.apod

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.paylocity.repository.model.Apod
import com.paylocity.unittest.databinding.ItemApodBinding

internal class ApodAdapter() : ListAdapter<Apod, ApodViewHolder>(ApodDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ApodViewHolder(ItemApodBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ApodViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            title.text = item.title
            image.load(item.url)
        }
    }
}

internal class ApodViewHolder(val binding: ItemApodBinding) : RecyclerView.ViewHolder(binding.root)

private class ApodDiffUtil : DiffUtil.ItemCallback<Apod>() {
    override fun areItemsTheSame(oldItem: Apod, newItem: Apod) = oldItem == newItem

    override fun areContentsTheSame(oldItem: Apod, newItem: Apod) =
        oldItem.url == newItem.url && oldItem.title == newItem.title
}