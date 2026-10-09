package com.hastakala.app.ui.inventory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.hastakala.app.AppConstants
import com.hastakala.app.R
import com.hastakala.app.databinding.FragmentInventoryBinding
import com.hastakala.app.viewmodel.InventoryViewModel

class InventoryFragment : Fragment() {

    private var _binding: FragmentInventoryBinding? = null
    private val binding get() = _binding!!

    private val inventoryViewModel: InventoryViewModel by viewModels()
    private lateinit var adapter: InventoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInventoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = InventoryAdapter { inventory ->
            showEditStockDialog(inventory.productName, inventory.colour, inventory.stockQuantity)
        }

        binding.rvInventory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvInventory.adapter = adapter

        binding.fabAddStock.setOnClickListener {
            showAddStockDialog()
        }

        inventoryViewModel.allInventory.observe(viewLifecycleOwner) { items ->
            adapter.submitList(items)
            binding.tvEmptyState.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
            binding.rvInventory.visibility = if (items.isEmpty()) View.GONE else View.VISIBLE

            val hasLowStock = items.any { it.stockQuantity < AppConstants.LOW_STOCK_THRESHOLD }
            binding.tvLowStockBanner.visibility = if (hasLowStock) View.VISIBLE else View.GONE
        }
    }

    private fun showAddStockDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_set_stock, null)
        val spinnerProduct = dialogView.findViewById<com.google.android.material.textfield.MaterialAutoCompleteTextView>(R.id.spinnerDialogProduct)
        val spinnerColour = dialogView.findViewById<com.google.android.material.textfield.MaterialAutoCompleteTextView>(R.id.spinnerDialogColour)
        val etQty = dialogView.findViewById<TextInputEditText>(R.id.etDialogQty)
        val tilProduct = dialogView.findViewById<TextInputLayout>(R.id.tilDialogProduct)
        val tilColour = dialogView.findViewById<TextInputLayout>(R.id.tilDialogColour)
        val tilQty = dialogView.findViewById<TextInputLayout>(R.id.tilDialogQty)

        spinnerProduct.setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, AppConstants.PRODUCTS))
        spinnerColour.setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, AppConstants.COLOURS))

        AlertDialog.Builder(requireContext())
            .setTitle("Set Stock Quantity")
            .setView(dialogView)
            .setPositiveButton("Save", null)
            .setNegativeButton("Cancel", null)
            .create()
            .apply {
                show()
                getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    val product = spinnerProduct.text.toString().trim()
                    val colour = spinnerColour.text.toString().trim()
                    val qty = etQty.text.toString().toIntOrNull()

                    var valid = true
                    if (product.isEmpty() || !AppConstants.PRODUCTS.contains(product)) { tilProduct.error = "Select a product"; valid = false } else tilProduct.error = null
                    if (colour.isEmpty() || !AppConstants.COLOURS.contains(colour)) { tilColour.error = "Select a colour"; valid = false } else tilColour.error = null
                    if (qty == null || qty < 0) { tilQty.error = "Enter a valid quantity"; valid = false } else tilQty.error = null

                    if (valid) {
                        inventoryViewModel.setStock(product, colour, qty!!)
                        dismiss()
                    }
                }
            }
    }

    private fun showEditStockDialog(product: String, colour: String, currentQty: Int) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_stock, null)
        val etQty = dialogView.findViewById<TextInputEditText>(R.id.etEditQty)
        val tilQty = dialogView.findViewById<TextInputLayout>(R.id.tilEditQty)
        etQty.setText(currentQty.toString())

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Stock: $colour $product")
            .setView(dialogView)
            .setPositiveButton("Save", null)
            .setNegativeButton("Cancel", null)
            .create()
            .apply {
                show()
                getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    val qty = etQty.text.toString().toIntOrNull()
                    if (qty == null || qty < 0) {
                        tilQty.error = "Enter a valid quantity"
                    } else {
                        inventoryViewModel.setStock(product, colour, qty)
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
