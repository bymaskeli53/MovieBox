package com.example.moviebox.ui

import androidx.fragment.app.Fragment
import com.example.moviebox.R
import com.example.moviebox.databinding.FragmentMoviesBinding
import com.example.moviebox.util.autoCleared
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesFragment : Fragment(R.layout.fragment_movies) {
    private var binding: FragmentMoviesBinding by autoCleared()
}
