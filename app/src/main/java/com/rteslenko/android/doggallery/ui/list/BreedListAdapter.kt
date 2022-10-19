package com.rteslenko.android.doggallery.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rteslenko.android.doggallery.R
import com.rteslenko.android.doggallery.data.model.Breed

class BreedListAdapter(private val onClick: (Breed) -> Unit)
    : ListAdapter<Breed, BreedListAdapter.BreedViewHolder>(object : DiffUtil.ItemCallback<Breed>() {
    override fun areItemsTheSame(oldItem: Breed, newItem: Breed): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Breed, newItem: Breed): Boolean {
        return oldItem.name.equals(newItem.name, true)
    }
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreedViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.breed_item, parent, false)
        return BreedViewHolder(view)
    }

    override fun onBindViewHolder(holder: BreedViewHolder, position: Int) {
        val breed = getItem(position)
        holder.bind(breed)
    }

    inner class BreedViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(breed: Breed) {
            with(itemView) {
                setOnClickListener {
                    onClick(breed)
                }
                findViewById<TextView>(R.id.bread_title).text = breed.name.replaceFirstChar {
                    it.uppercase()
                }
            }
        }
    }
}
