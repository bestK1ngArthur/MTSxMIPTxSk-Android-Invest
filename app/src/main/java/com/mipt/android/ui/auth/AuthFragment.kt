package com.mipt.android.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.viewBinding
import com.mipt.android.databinding.AuthFragmentBinding
import com.mipt.android.R

class AuthFragment : Fragment(R.layout.auth_fragment) {
    private val viewBinding by viewBinding(AuthFragmentBinding::bind)
    private val viewModel by viewModels<AuthViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewBinding) {
            viewModel.error.observe(viewLifecycleOwner, Observer { error ->
                val toast = Toast.makeText(context, error, Toast.LENGTH_LONG)
                toast.show()
            })

            applyButton.setOnClickListener {
                viewModel.onApply(tokenTextView.text.toString())
            }
        }
    }
}