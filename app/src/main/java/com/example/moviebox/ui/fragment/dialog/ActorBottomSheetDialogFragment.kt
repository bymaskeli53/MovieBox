package com.example.moviebox.ui.fragment.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.CircleCropTransformation
import com.example.moviebox.R
import com.example.moviebox.databinding.ActorBottomSheetDialogBinding
import com.example.moviebox.model.Cast
import com.example.moviebox.util.Gender
import com.example.moviebox.util.autoCleared
import com.example.moviebox.util.constant.DurationConstants.CROSSFADE_DURATION
import com.example.moviebox.util.constant.NetworkConstants.IMAGE_BASE_URL
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

        val actor = navArgs.actor

        binding.tvActorName.text = actor.name
        binding.tvPopularity.text = getString(R.string.popularity) + actor.popularity.toString()
        binding.tvCharacter.text = actor.character
        binding.tvGender.text = genderDecider(actor.gender)
        setActorImage(actor)
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

    private fun setActorImage(actor: Cast) {
        binding.ivActor.load(IMAGE_BASE_URL + actor.profile_path) {
            crossfade(CROSSFADE_DURATION)
            transformations(CircleCropTransformation())
            placeholder(R.drawable.ic_generic_movie_poster)
            error(R.drawable.ic_generic_movie_poster)
        }
    }

    private fun genderDecider(gender: Int): String {
        val genderEnum = Gender.fromInt(gender)
        return getString(genderEnum.labelResourceId)
    }
}
