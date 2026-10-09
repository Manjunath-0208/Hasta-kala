package com.hastakala.app.ui.sale

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.hastakala.app.AppConstants
import com.hastakala.app.databinding.FragmentAddSaleBinding
import com.hastakala.app.viewmodel.SaleViewModel

class AddSaleFragment : Fragment() {

    private var _binding: FragmentAddSaleBinding? = null
    private val binding get() = _binding!!

    private val saleViewModel: SaleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddSaleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDropdowns()
        setupSaveButton()
        observeViewModel()
    }

    private fun setupDropdowns() {
        val productAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            AppConstants.PRODUCTS
        )
        binding.spinnerProduct.setAdapter(productAdapter)

        val colourAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            AppConstants.COLOURS
        )
        binding.spinnerColour.setAdapter(colourAdapter)
    }

    private fun setupSaveButton() {
        binding.btnSaveSale.setOnClickListener {
            if (validateInputs()) {
                val product = binding.spinnerProduct.text.toString()
                val colour = binding.spinnerColour.text.toString()
                val quantity = binding.etQuantity.text.toString().toIntOrNull() ?: 1
                saleViewModel.insertSale(product, colour, quantity)
            }
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        val product = binding.spinnerProduct.text.toString().trim()
        if (product.isEmpty() || !AppConstants.PRODUCTS.contains(product)) {
            binding.tilProduct.error = "Please select a product"
            isValid = false
        } else {
            binding.tilProduct.error = null
        }

        val colour = binding.spinnerColour.text.toString().trim()
        if (colour.isEmpty() || !AppConstants.COLOURS.contains(colour)) {
            binding.tilColour.error = "Please select a colour"
            isValid = false
        } else {
            binding.tilColour.error = null
        }

        val qtyText = binding.etQuantity.text.toString().trim()
        val qty = qtyText.toIntOrNull()
        if (qty == null || qty < 1) {
            binding.tilQuantity.error = "Quantity must be at least 1"
            isValid = false
        } else {
            binding.tilQuantity.error = null
        }

        return isValid
    }

    private fun observeViewModel() {
        saleViewModel.saveSuccess.observe(viewLifecycleOwner) { success ->
            if (success == true) {
                // Check for stock alert before navigating back
                if (saleViewModel.stockAlert.value == null) {
                    Toast.makeText(requireContext(), "Sale saved successfully!", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            } else if (success == false) {
                Toast.makeText(requireContext(), "Save failed. Please try again.", Toast.LENGTH_SHORT).show()
            }
        }

        saleViewModel.stockAlert.observe(viewLifecycleOwner) { alert ->
            if (alert != null) {
                showStockAlertDialog(alert.product, alert.colour, alert.remaining)
            }
        }
    }

    private fun showStockAlertDialog(product: String, colour: String, remaining: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("⚠️ Low Stock Alert")
            .setMessage("$colour $product is low on stock — only $remaining units remaining. Time to restock!")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                saleViewModel.clearStockAlert()
                Toast.makeText(requireContext(), "Sale saved successfully!", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
            .setCancelable(false)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
