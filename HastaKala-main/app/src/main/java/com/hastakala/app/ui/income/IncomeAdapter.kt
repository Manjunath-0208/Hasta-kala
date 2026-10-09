package com.hastakala.app.ui.income

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hastakala.app.DateUtils
import com.hastakala.app.data.entities.Income
import com.hastakala.app.databinding.ItemIncomeBinding

class IncomeAdapter : ListAdapter<Income, IncomeAdapter.IncomeViewHolder>(IncomeDiffCallback()) {

    inner class IncomeViewHolder(private val binding: ItemIncomeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(income: Income) {
            binding.tvAmount.text = "Rs. %.2f".format(income.amount)
            binding.tvDate.text = DateUtils.formatDateOnly(income.incomeDate)

            if (!income.source.isNullOrBlank()) {
                binding.tvSource.text = income.source
                binding.tvSource.visibility = View.VISIBLE
            } else {
                binding.tvSource.visibility = View.GONE
            }

            if (!income.note.isNullOrBlank()) {
                binding.tvNote.text = income.note
                binding.tvNote.visibility = View.VISIBLE
            } else {
                binding.tvNote.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomeViewHolder {
        val binding = ItemIncomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IncomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IncomeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class IncomeDiffCallback : DiffUtil.ItemCallback<Income>() {
        override fun areItemsTheSame(oldItem: Income, newItem: Income) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Income, newItem: Income) = oldItem == newItem
    }
}
