package com.hastakala.app.ui.sale

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hastakala.app.databinding.FragmentSalesHistoryBinding
import com.hastakala.app.viewmodel.SaleViewModel

class SalesHistoryFragment : Fragment() {

    private var _binding: FragmentSalesHistoryBinding? = null
    private val binding get() = _binding!!

    private val saleViewModel: SaleViewModel by viewModels()
    private lateinit var adapter: SaleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSalesHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SaleAdapter { sale ->
            AlertDialog.Builder(requireContext())
                .setTitle("Delete Sale Record")
                .setMessage("Delete this sale record?")
                .setPositiveButton("Delete") { _, _ -> saleViewModel.deleteSale(sale) }
                .setNegativeButton("Cancel", null)
                .show()
        }

        binding.rvSales.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSales.adapter = adapter

        saleViewModel.allSales.observe(viewLifecycleOwner) { sales ->
            adapter.submitList(sales)
            binding.tvEmptyState.visibility = if (sales.isEmpty()) View.VISIBLE else View.GONE
            binding.rvSales.visibility = if (sales.isEmpty()) View.GONE else View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
