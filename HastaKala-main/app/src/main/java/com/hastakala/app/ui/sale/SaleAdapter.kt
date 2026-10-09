package com.hastakala.app.ui.sale

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hastakala.app.DateUtils
import com.hastakala.app.data.entities.Sale
import com.hastakala.app.databinding.ItemSaleBinding

class SaleAdapter(
    private val onDeleteClick: (Sale) -> Unit
) : ListAdapter<Sale, SaleAdapter.SaleViewHolder>(SaleDiffCallback()) {

    inner class SaleViewHolder(private val binding: ItemSaleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(sale: Sale) {
            binding.tvProductName.text = sale.productName
            binding.tvColour.text = sale.colour
            binding.tvQuantity.text = "Qty: ${sale.quantity}"
            binding.tvDate.text = DateUtils.formatTimestamp(sale.saleDate)
            binding.btnDelete.setOnClickListener { onDeleteClick(sale) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaleViewHolder {
        val binding = ItemSaleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SaleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SaleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SaleDiffCallback : DiffUtil.ItemCallback<Sale>() {
        override fun areItemsTheSame(oldItem: Sale, newItem: Sale) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Sale, newItem: Sale) = oldItem == newItem
    }
}
