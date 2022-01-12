package com.mipt.android.tools

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.mipt.android.R.anim
import com.mipt.android.R.id

fun FragmentManager.navigate(fragment: Fragment, isBack: Boolean = false) {
    commit(allowStateLoss = true) {
        if (isBack) {
            setCustomAnimations(
                anim.slide_from_left,
                anim.slide_to_right
            )
        } else {
            setCustomAnimations(
                anim.slide_from_right,
                anim.slide_to_left
            )
        }

        replace(id.main_activity_container, fragment)
        addToBackStack(null)
    }
}