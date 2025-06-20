package com.lesson.codewalledu.src.ui.views.core.explore.cheat_sheets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.lesson.codewalledu.R // Assuming R is accessible
import com.lesson.codewalledu.databinding.ItemCheatSheetImageBinding
import com.lesson.codewalledu.databinding.LayoutCardCheatSheets2Binding
import com.squareup.picasso.Picasso

class CheatSheetImageFragment : Fragment(R.layout.layout_card_cheat_sheets2) {

    private var imageUrl: String? = null
    lateinit var binding: LayoutCardCheatSheets2Binding

    companion object {
        private const val ARG_IMAGE_URL = "image_url"

        fun newInstance(imageUrl: String): CheatSheetImageFragment {
            val fragment = CheatSheetImageFragment()
            val args = Bundle()
            args.putString(ARG_IMAGE_URL, imageUrl)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imageUrl = it.getString(ARG_IMAGE_URL)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding = LayoutCardCheatSheets2Binding.bind(view)
         // Make sure this ID is correct for item_cheat_sheet_image

        imageUrl?.let { url ->
            Picasso.get()
                .load(url)
                .placeholder(R.drawable.rectangle_placeholder) // You can use a better placeholder
                .error(R.drawable.baseline_running_with_errors_24) // Your error drawable
                .into(binding.imageView)
        } ?: run {
            // Optional: handle case where imageUrl is null
            binding.imageView.setImageResource(R.drawable.baseline_running_with_errors_24)
        }
    }
}