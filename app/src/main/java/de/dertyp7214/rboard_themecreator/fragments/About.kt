package de.dertyp7214.rboard_themecreator.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import de.dertyp7214.rboard_themecreator.R

class About(private val scrollState: Int = 0) : BaseFragment() {

    private var elevate = false
    private lateinit var activity: AppCompatActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_about, container, false)

        activity = getActivity()!! as AppCompatActivity

        activity.title = getString(R.string.about)

        return v
    }
}
