package com.mipt.android.ui.portfolio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.mipt.android.R
import com.mipt.android.databinding.DetailsFragmentBinding
import com.mipt.android.tools.navigate
import com.mipt.android.ui.portfolio.PortfolioFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.details_fragment) {
    private val viewBinding by viewBinding(DetailsFragmentBinding::bind)
    private val viewModel by viewModels<DetailsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getStockInfo()
        viewModel.setCandleStickChart()

        with(viewBinding) {
            backButton.setOnClickListener {
                showDetails()
            }
            viewModel.stockName.observe(viewLifecycleOwner, { stockName ->
                if (stockName != null) {
                    stockNameTextView.text = "$stockName"
                } else {
                    stockNameTextView.text = null
                }
            })

            viewModel.candleStickChart.observe(viewLifecycleOwner, { candleStickChart ->
                if (candleStickChart != null) {

                } else {
                }
            })


        }
    }



    private fun showDetails() {
        parentFragmentManager.navigate(PortfolioFragment(), true)
    }
}