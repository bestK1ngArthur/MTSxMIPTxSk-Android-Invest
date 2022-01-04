package com.mipt.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mipt.android.ui.auth.AuthFragment
import com.mipt.android.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, AuthFragment())
                    .commitNow()
        }
    }
}