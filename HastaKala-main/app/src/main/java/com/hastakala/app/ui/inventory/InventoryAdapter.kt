package com.hastakala.app.ui.inventory

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hastakala.app.AppConstants
import com.hastakala.app.data.entities.Inventory
import com.hastakala.app.databinding.ItemInventoryBinding

class InventoryAdapter(
    private val onEditClick: (Inventory) -> Unit
) : ListAdapter<Inventory, InventoryAdapter.InventoryViewHolder>(InventoryDiffCallback()) {

    inner class InventoryViewHolder(private val binding: ItemInventoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(inventory: Inventory) {
            binding.tvProductName.text = inventory.productName
            binding.tvColour.text = inventory.colour
            binding.tvStockQty.text = "${inventory.stockQuantity} units"

            if (inventory.stockQuantity < AppConstants.LOW_STOCK_THRESHOLD) {
                binding.tvStockQty.setTextColor(Color.parseColor("#E53935"))
                binding.tvLowStockLabel.visibility = android.view.View.VISIBLE
                binding.cardInventory.setCardBackgroundColor(Color.parseColor("#FFF8F8"))
            } else {
                binding.tvStockQty.setTextColor(Color.parseColor("#212121"))
                binding.tvLowStockLabel.visibility = android.view.View.GONE
                binding.cardInventory.setCardBackgroundColor(Color.WHITE)
            }

            binding.btnEditStock.setOnClickListener { onEditClick(inventory) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        val binding = ItemInventoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InventoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class InventoryDiffCallback : DiffUtil.ItemCallback<Inventory>() {
        override fun areItemsTheSame(oldItem: Inventory, newItem: Inventory) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Inventory, newItem: Inventory) = oldItem == newItem
    }
}
