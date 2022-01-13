package com.mipt.android.ui.portfolio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.mipt.android.R
import com.mipt.android.databinding.DetailsFragmentBinding
import com.mipt.android.tools.navigate
import com.mipt.android.ui.details.Figi
import com.mipt.android.ui.portfolio.PortfolioFragment
import dagger.hilt.android.AndroidEntryPoint
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
    private val viewModel by viewModels<DetailsViewModel>() {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                viewModelFactory.create(arguments?.getParcelable(DETAILS_FIGI_KEY)!!) as T
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getStockInfo()

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

        }
    }

    private fun showDetails() {
        parentFragmentManager.navigate(PortfolioFragment(), true)
    }
}