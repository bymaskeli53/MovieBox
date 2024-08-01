package com.example.moviebox.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.moviebox.R
import com.example.moviebox.databinding.FragmentDetailsBinding
import com.example.moviebox.util.autoCleared
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {
    private var binding: FragmentDetailsBinding by autoCleared()

    private val args: DetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailsBinding.bind(view)

       binding.tvMovieTitle.text =  args.movie.title
       binding.tvMovieOverview.text =  args.movie.overview
        binding.tvMovieReleaseDate.text = args.movie.release_date

    }
}
