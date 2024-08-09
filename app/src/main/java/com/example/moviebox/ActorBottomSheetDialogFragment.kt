package com.example.moviebox

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.moviebox.databinding.ActorBottomSheetDialogBinding
import com.example.moviebox.util.autoCleared
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ActorBottomSheetDialogFragment : BottomSheetDialogFragment() {
    private var binding: ActorBottomSheetDialogBinding by autoCleared()

    private val navArgs: ActorBottomSheetDialogFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = inflater.inflate(R.layout.actor_bottom_sheet_dialog, container, false)

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = ActorBottomSheetDialogBinding.bind(view)

        binding.textView.text = navArgs.actor.name
        binding.textView2.text = navArgs.actor.popularity.toString()
        binding.textView3.text = navArgs.actor.character
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        super.onCreateDialog(savedInstanceState).apply {
            setCanceledOnTouchOutside(true)
            // window?.attributes?.windowAnimations = com.google.android.material.R.style.Widget_AppCompat_Light_ListPopupWindow
        }

    override fun onStart() {
        super.onStart()

        dialog?.let {
            val bottomSheet =
                it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let { sheet ->
                val behavior = BottomSheetBehavior.from(sheet)
                val layoutParams = sheet.layoutParams as ViewGroup.MarginLayoutParams

                layoutParams.setMargins(32, 0, 32, 100) // Set the desired margins here
                sheet.layoutParams = layoutParams

                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }
}
