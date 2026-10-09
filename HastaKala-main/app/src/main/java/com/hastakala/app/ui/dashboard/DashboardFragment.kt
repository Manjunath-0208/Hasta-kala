package com.hastakala.app.ui.dashboard

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.hastakala.app.AppConstants
import com.hastakala.app.R
import com.hastakala.app.databinding.FragmentDashboardBinding
import com.hastakala.app.viewmodel.AnalyticsViewModel
import com.hastakala.app.viewmodel.SaleViewModel

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val saleViewModel: SaleViewModel by viewModels()
    private val analyticsViewModel: AnalyticsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCharts()
        setupFilterChips()
        setupFab()
        observeData()
    }

    private fun setupFab() {
        binding.fabAddSale.setOnClickListener {
            findNavController().navigate(R.id.action_dashboard_to_addSale)
        }
    }

    private fun setupFilterChips() {
        binding.chipAllTime.isChecked = true

        binding.chipGroupFilter.setOnCheckedStateChangeListener { _, checkedIds ->
            when {
                checkedIds.contains(R.id.chipThisWeek) -> analyticsViewModel.setPeriod(AppConstants.Period.THIS_WEEK)
                checkedIds.contains(R.id.chipThisMonth) -> analyticsViewModel.setPeriod(AppConstants.Period.THIS_MONTH)
                else -> analyticsViewModel.setPeriod(AppConstants.Period.ALL_TIME)
            }
        }
    }

    private fun setupCharts() {
        // Pie chart styling
        binding.pieChart.apply {
            description.isEnabled = false
            isDrawHoleEnabled = true
            holeRadius = 40f
            setHoleColor(Color.WHITE)
            setTransparentCircleAlpha(0)
            setUsePercentValues(true)
            legend.apply {
                isEnabled = true
                orientation = Legend.LegendOrientation.VERTICAL
                horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                verticalAlignment = Legend.LegendVerticalAlignment.CENTER
                setDrawInside(false)
            }
            setNoDataText("No sales recorded yet")
            setNoDataTextColor(Color.GRAY)
        }

        // Bar chart styling
        binding.barChart.apply {
            description.isEnabled = false
            setFitBars(true)
            axisRight.isEnabled = false
            axisLeft.granularity = 1f
            axisLeft.setDrawGridLines(false)
            xAxis.granularity = 1f
            xAxis.setDrawGridLines(false)
            legend.isEnabled = false
            setNoDataText("No sales recorded yet")
            setNoDataTextColor(Color.GRAY)
        }
    }

    private fun observeData() {
        saleViewModel.totalSaleCount.observe(viewLifecycleOwner) { count ->
            binding.tvTotalSales.text = count?.toString() ?: "0"
        }

        analyticsViewModel.colourSums.observe(viewLifecycleOwner) { colourSums ->
            updatePieChart(colourSums ?: emptyList())
            updateTopSeller(colourSums ?: emptyList(), null)
        }

        analyticsViewModel.productSums.observe(viewLifecycleOwner) { productSums ->
            updateBarChart(productSums ?: emptyList())
            analyticsViewModel.colourSums.value?.let { colourSums ->
                updateTopSeller(colourSums, productSums)
            }
        }
    }

    private fun updatePieChart(colourSums: List<com.hastakala.app.data.dao.ColourSum>) {
        if (colourSums.isEmpty()) {
            binding.pieChart.clear()
            binding.pieChart.invalidate()
            return
        }

        val colours = AppConstants.COLOURS
        val entries = colourSums.map { PieEntry(it.total.toFloat(), it.colour) }
        val dataSet = PieDataSet(entries, "").apply {
            colors = entries.map { entry ->
                val idx = colours.indexOf(entry.label).takeIf { it >= 0 } ?: 0
                AppConstants.CHART_COLOURS[idx % AppConstants.CHART_COLOURS.size]
            }
            valueTextSize = 12f
            valueTextColor = Color.WHITE
            sliceSpace = 2f
        }

        binding.pieChart.data = PieData(dataSet)
        binding.pieChart.animateY(600)
        binding.pieChart.invalidate()
    }

    private fun updateBarChart(productSums: List<com.hastakala.app.data.dao.ProductSum>) {
        if (productSums.isEmpty()) {
            binding.barChart.clear()
            binding.barChart.invalidate()
            return
        }

        val entries = productSums.mapIndexed { index, item ->
            BarEntry(index.toFloat(), item.total.toFloat())
        }

        val labels = productSums.map { it.productName }

        val dataSet = BarDataSet(entries, "Units Sold").apply {
            color = 0xFF3F51B5.toInt()
            valueTextSize = 11f
            valueTextColor = Color.DKGRAY
        }

        binding.barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        binding.barChart.xAxis.labelCount = labels.size
        binding.barChart.data = BarData(dataSet)
        binding.barChart.animateY(600)
        binding.barChart.invalidate()
    }

    private fun updateTopSeller(
        colourSums: List<com.hastakala.app.data.dao.ColourSum>,
        productSums: List<com.hastakala.app.data.dao.ProductSum>?
    ) {
        val topColour = colourSums.firstOrNull()?.colour ?: "—"
        val topProduct = (productSums ?: analyticsViewModel.productSums.value)?.firstOrNull()?.productName ?: "—"
        binding.tvTopProduct.text = topProduct
        binding.tvTopColour.text = topColour
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
