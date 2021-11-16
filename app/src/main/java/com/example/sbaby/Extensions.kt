package com.example.sbaby

import androidx.fragment.app.Fragment
import androidx.fragment.app.commit

fun Fragment.startFragment(fragment: Fragment) {
    parentFragmentManager.commit {
        setReorderingAllowed(true)
        replace(R.id.fragment_container, fragment)
        addToBackStack(fragment.toString())
    }
}