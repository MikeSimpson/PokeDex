package nz.co.mikesimpson.pokedex.ui.common

import android.view.LayoutInflater
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import nz.co.mikesimpson.pokedex.R
import nz.co.mikesimpson.pokedex.databinding.ListItemBinding
import nz.co.mikesimpson.pokedex.model.ListItem

class ListItemAdapter(
    val onItemClick: (ListItem) -> Unit
) : ListAdapter<ListItem, ListItemViewHolder>(
    AsyncDifferConfig.Builder<ListItem>(
        object : DiffUtil.ItemCallback<ListItem>() {
            override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean =
                oldItem.name == newItem.name
        }
    ).build()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val binding = DataBindingUtil.inflate<ListItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.list_item,
            parent,
            false
        )
        binding.root.setOnClickListener {
            binding.item?.let{
                onItemClick(it)
            }
        }
        return ListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        holder.binding.item = getItem(position)
        holder.binding.executePendingBindings()
    }
}

class ListItemViewHolder constructor(val binding: ListItemBinding) :
    RecyclerView.ViewHolder(binding.root)