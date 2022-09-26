package com.anless.rentmotors.ui.profile

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.anless.rentmotors.R
import android.view.LayoutInflater
import androidx.fragment.app.Fragment

class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }
}