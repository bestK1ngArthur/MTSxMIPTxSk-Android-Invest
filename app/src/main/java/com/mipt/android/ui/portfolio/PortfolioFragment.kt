package com.mipt.android.ui.portfolio

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.mipt.android.R
import com.mipt.android.data.api.responses.portfolio.PortfolioResponse
import com.mipt.android.databinding.PortfolioFragmentBinding
import com.mipt.android.tools.navigate
import com.mipt.android.ui.auth.AuthFragment
import com.mipt.android.ui.details.Figi
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class PortfolioFragment : Fragment(R.layout.portfolio_fragment) {
    private val viewBinding by viewBinding(PortfolioFragmentBinding::bind)
    private val viewModel by viewModels<PortfolioViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewBinding) {
            profileButton.setOnClickListener {
                showAuth()
            }

            viewModel.toast.observe(viewLifecycleOwner, { error ->
                val toast = Toast.makeText(context, error, Toast.LENGTH_LONG)
                toast.show()
            });

            viewModel.result.observe(viewLifecycleOwner, { array ->
                val listView: ListView = listPortfoli
                val adapter = activity?.let { it -> HistoryItemAdapter(it, array) { figi -> showDetails(figi) } }
                listView.adapter = adapter
            })

        }
    }

    private fun showAuth() {
        parentFragmentManager.navigate(AuthFragment(), true)
    }

    private fun showDetails(figi: String) {
        parentFragmentManager.navigate(
            DetailsFragment.newInstance(Figi(figi))
        )
    }
}
