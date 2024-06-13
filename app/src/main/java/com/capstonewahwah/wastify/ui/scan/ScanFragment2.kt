package com.capstonewahwah.wastify.ui.scan

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import androidx.transition.Fade
import com.capstonewahwah.wastify.R
import com.capstonewahwah.wastify.databinding.FragmentScan2Binding

class ScanFragment2 : Fragment() {

    private var _binding: FragmentScan2Binding? = null
    private val binding get() = _binding

    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = Fade()
        exitTransition = Fade()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScan2Binding.inflate(inflater, container, false)
        return binding?.root
    }

    private val launchGallery = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No Media Selected")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.cvAsBtnGallery?.setOnClickListener {
            launchGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            val toPreviewFragment = ScanFragment2Directions.actionNavigationScan2ToNavigationPreview()
            toPreviewFragment.uri = it.toString()
            findNavController().navigate(toPreviewFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}