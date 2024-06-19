package com.fouranger.wastify.ui.main.scan

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.transition.Fade
import com.fouranger.wastify.databinding.FragmentScan2Binding
import com.fouranger.wastify.helper.Utils.getImageUri

class ScanFragment2 : Fragment() {

    private var _binding: FragmentScan2Binding? = null
    private val binding get() = _binding

    private var currentImageUri: Uri? = null

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        when {
            permissions[Manifest.permission.CAMERA] ?: false -> {
                Toast.makeText(requireContext(), "Permission Request Granted", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(requireContext(), "Permission Request Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED
    }

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

    private val launchIntentCamera = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess: Boolean ->
        if (isSuccess) showImage()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.cvAsBtnCam?.setOnClickListener {
            if (checkPermission(Manifest.permission.CAMERA)) {
                currentImageUri = getImageUri(requireContext())
                launchIntentCamera.launch(currentImageUri)
            } else {
                requestPermissionLauncher.launch(arrayOf(
                    Manifest.permission.CAMERA
                ))
            }
        }

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