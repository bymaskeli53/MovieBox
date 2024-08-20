package com.example.moviebox.ui.fragment.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.moviebox.util.autoCleared
import dagger.hilt.android.AndroidEntryPoint


abstract class BaseFragment<T : ViewBinding>(
    private val bind: (View) -> T,
    @LayoutRes private val layoutResId: Int,
) : Fragment(layoutResId) {
    protected var binding: T by autoCleared()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = bind(view)
    }
}
