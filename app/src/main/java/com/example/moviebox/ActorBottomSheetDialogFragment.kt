package com.example.moviebox

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.moviebox.databinding.ActorBottomSheetDialogBinding
import com.example.moviebox.databinding.FragmentMoviesBinding
import com.example.moviebox.util.autoCleared
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ActorBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private var binding: ActorBottomSheetDialogBinding by autoCleared()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.actor_bottom_sheet_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ActorBottomSheetDialogBinding.bind(view)



    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setCanceledOnTouchOutside(true)
        }

    }

}