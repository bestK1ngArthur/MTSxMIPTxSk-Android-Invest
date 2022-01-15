package com.mipt.android.ui.details

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.mipt.android.R
import com.mipt.android.databinding.DetailsFragmentBinding
import com.mipt.android.tools.navigate
import com.mipt.android.ui.portfolio.PortfolioFragment
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.details_fragment) {
    companion object {
        fun newInstance(figi: Figi) = DetailsFragment().apply {
            arguments = bundleOf(DETAILS_FIGI_KEY to figi)
        }

        private const val DETAILS_FIGI_KEY = "DETAILS_FIGI_KEY"
    }

    @Inject
    lateinit var viewModelFactory: DetailsViewModel.Factory

    private val viewBinding by viewBinding(DetailsFragmentBinding::bind)
    private val viewModel by viewModels<DetailsViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                viewModelFactory.create(arguments?.getParcelable(DETAILS_FIGI_KEY)!!) as T
        }
    }

    private var fromCalendar = Calendar.getInstance()
    private var toCalendar = Calendar.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCandleArray("month")
        viewModel.getStockInfo()

        with(viewBinding) {
            backButton.setOnClickListener {
                showDetails()
            }

            day.setOnClickListener {
                viewModel.getCandleArray("day")
            }

            week.setOnClickListener {
                viewModel.getCandleArray("week")
            }

            month.setOnClickListener {
                viewModel.getCandleArray("month")
            }

            viewModel.candleStickChart.observe(viewLifecycleOwner, { candleStickChart ->

                if (candleStickChart != null) {
                    candleStickChartImg.data = candleStickChart

                } else {
                    candleStickChartImg.data = null
                }
                candleStickChartImg.setDescription("Свечи")
                candleStickChartImg.invalidate()
            })

            viewModel.stockName.observe(viewLifecycleOwner, { stockName ->
                if (stockName != null) {
                    stockNameTextView.text = "$stockName"
                } else {
                    stockNameTextView.text = null
                }
            })

            viewModel.stockCurrency.observe(viewLifecycleOwner, { stockCurrency ->
                if (stockCurrency != null) {
                    currencyTextView.text = "Валюта: $stockCurrency"
                } else {
                    currencyTextView.text = null
                }
            })

            viewModel.lastPrice.observe(viewLifecycleOwner, { lastPrice ->
                if (lastPrice != null) {
                    lastPriceTextView.text = "Последняя цена: $lastPrice"
                } else {
                    lastPriceTextView.text = null
                }
            })

            viewModel.fromDate.observe(viewLifecycleOwner, { fromDate ->
                fromCalendar.time = fromDate
                fromDateTextView.text = SimpleDateFormat("dd.MM.yyyy").format(fromDate)
            })

            viewModel.toDate.observe(viewLifecycleOwner, { toDate ->
                toCalendar.time = toDate
                toDateTextView.text = SimpleDateFormat("dd.MM.yyyy").format(toDate)
            })

            val fromDateSetListener =
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    fromCalendar.set(Calendar.YEAR, year)
                    fromCalendar.set(Calendar.MONTH, monthOfYear)
                    fromCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    viewModel.setFromDate(fromCalendar.time)
                }

            fromDateTextView.setOnClickListener {
                DatePickerDialog(
                    requireContext(), fromDateSetListener,
                    fromCalendar.get(Calendar.YEAR),
                    fromCalendar.get(Calendar.MONTH),
                    fromCalendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

            val toDateSetListener =
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    toCalendar.set(Calendar.YEAR, year)
                    toCalendar.set(Calendar.MONTH, monthOfYear)
                    toCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    viewModel.setToDate(toCalendar.time)
                }

            toDateTextView.setOnClickListener {
                DatePickerDialog(
                    requireContext(), toDateSetListener,
                    toCalendar.get(Calendar.YEAR),
                    toCalendar.get(Calendar.MONTH),
                    toCalendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

            viewModel.toast.observe(viewLifecycleOwner, { error ->
                val toast = Toast.makeText(context, error, Toast.LENGTH_LONG)
                toast.show()
            })
        }
    }

    private fun showDetails() {
        parentFragmentManager.navigate(PortfolioFragment(), true)
    }
}