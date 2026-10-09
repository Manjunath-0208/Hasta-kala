package com.hastakala.app.ui.income

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.hastakala.app.R
import com.hastakala.app.databinding.FragmentIncomeBinding
import com.hastakala.app.viewmodel.IncomeViewModel

class IncomeFragment : Fragment() {

    private var _binding: FragmentIncomeBinding? = null
    private val binding get() = _binding!!

    private val incomeViewModel: IncomeViewModel by viewModels()
    private lateinit var adapter: IncomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIncomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = IncomeAdapter()
        binding.rvIncome.layoutManager = LinearLayoutManager(requireContext())
        binding.rvIncome.adapter = adapter

        binding.fabAddIncome.setOnClickListener { showAddIncomeDialog() }

        observeViewModel()
    }

    private fun observeViewModel() {
        incomeViewModel.allIncome.observe(viewLifecycleOwner) { incomeList ->
            adapter.submitList(incomeList)
            binding.tvEmptyState.visibility = if (incomeList.isEmpty()) View.VISIBLE else View.GONE
            binding.rvIncome.visibility = if (incomeList.isEmpty()) View.GONE else View.VISIBLE
        }

        incomeViewModel.totalIncomeAllTime.observe(viewLifecycleOwner) { total ->
            binding.tvTotalAllTime.text = "Rs. %.2f".format(total ?: 0.0)
        }

        incomeViewModel.totalIncomeThisMonth.observe(viewLifecycleOwner) { total ->
            binding.tvTotalThisMonth.text = "Rs. %.2f".format(total ?: 0.0)
        }

        incomeViewModel.totalIncomeThisWeek.observe(viewLifecycleOwner) { total ->
            binding.tvTotalThisWeek.text = "Rs. %.2f".format(total ?: 0.0)
        }

        incomeViewModel.saveSuccess.observe(viewLifecycleOwner) { success ->
            if (success == true) {
                Toast.makeText(requireContext(), "Income recorded!", Toast.LENGTH_SHORT).show()
            } else if (success == false) {
                Toast.makeText(requireContext(), "Save failed. Try again.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showAddIncomeDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_income, null)
        val etAmount = dialogView.findViewById<TextInputEditText>(R.id.etIncomeAmount)
        val etSource = dialogView.findViewById<TextInputEditText>(R.id.etIncomeSource)
        val etNote = dialogView.findViewById<TextInputEditText>(R.id.etIncomeNote)
        val tilAmount = dialogView.findViewById<TextInputLayout>(R.id.tilIncomeAmount)

        AlertDialog.Builder(requireContext())
            .setTitle("Add Income Entry")
            .setView(dialogView)
            .setPositiveButton("Save", null)
            .setNegativeButton("Cancel", null)
            .create()
            .apply {
                show()
                getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    val amount = etAmount.text.toString().toDoubleOrNull()
                    if (amount == null || amount <= 0) {
                        tilAmount.error = "Amount must be greater than 0"
                    } else {
                        tilAmount.error = null
                        val source = etSource.text.toString().trim()
                        val note = etNote.text.toString().trim()
                        incomeViewModel.insertIncome(amount, source, note)
                        dismiss()
                    }
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
