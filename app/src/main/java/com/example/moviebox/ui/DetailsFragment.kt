package com.example.moviebox.ui

import androidx.fragment.app.Fragment
import com.example.moviebox.R
import com.example.moviebox.databinding.FragmentDetailsBinding
import com.example.moviebox.util.autoCleared
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {
    private var binding: FragmentDetailsBinding by autoCleared()
}
