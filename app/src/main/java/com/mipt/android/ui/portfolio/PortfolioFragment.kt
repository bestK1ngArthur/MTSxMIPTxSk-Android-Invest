package com.mipt.android.ui.portfolio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.mipt.android.R
import com.mipt.android.databinding.PortfolioFragmentBinding
import com.mipt.android.tools.navigate
import com.mipt.android.ui.auth.AuthFragment
import dagger.hilt.android.AndroidEntryPoint

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
        }
    }

    private fun showAuth() {
        parentFragmentManager.navigate(AuthFragment())
    }
}