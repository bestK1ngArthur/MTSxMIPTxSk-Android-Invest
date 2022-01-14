package com.mipt.android.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.mipt.android.R
import com.mipt.android.tools.navigate
import com.mipt.android.ui.auth.AuthFragment
import com.mipt.android.ui.portfolio.PortfolioFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.navigate(if (viewModel.isAuthorized) PortfolioFragment() else AuthFragment())
        }
    }
}