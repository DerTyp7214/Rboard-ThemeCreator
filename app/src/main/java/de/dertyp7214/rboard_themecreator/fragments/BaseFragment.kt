package de.dertyp7214.rboard_themecreator.fragments

import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {

    open fun onBackPressed(): Boolean {
        return true
    }
}