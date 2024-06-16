package com.capstonewahwah.wastify.ui.main.scan

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.transition.Slide
import com.capstonewahwah.wastify.R
import com.capstonewahwah.wastify.data.remote.response.PredictResponse
import com.capstonewahwah.wastify.data.remote.response.Recommendations
import com.capstonewahwah.wastify.databinding.FragmentPreviewBinding
import com.capstonewahwah.wastify.helper.Utils.reduceFileImage
import com.capstonewahwah.wastify.helper.Utils.uriToFile
import com.capstonewahwah.wastify.helper.ViewModelFactory
import com.capstonewahwah.wastify.ui.main.MainViewModel
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class PreviewFragment : Fragment() {

    private var _binding: FragmentPreviewBinding? = null
    private val binding get() = _binding

    private var currentImageUri: Uri? = null

    private val scanViewModel by viewModels<ScanViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private val mainViewModel by activityViewModels<MainViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = Slide(Gravity.END)
        exitTransition = Slide(Gravity.START)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPreviewBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val image = PreviewFragmentArgs.fromBundle(arguments as Bundle).uri
        currentImageUri = image.toUri()
        binding?.ivPrev?.setImageURI(currentImageUri)

        binding?.btnAnalyze?.setOnClickListener {
            analyze()
        }

        scanViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            setLoading(isLoading)
        }
    }

    private fun analyze() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, requireContext()).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val imageMultiPartBody = MultipartBody.Part.createFormData(
                "image",
                imageFile.name,
                requestImageFile
            )

            mainViewModel.getSession().observe(viewLifecycleOwner) { user ->
                scanViewModel.predict(user.token, imageMultiPartBody)
                Log.d("PreviewFragToken", user.token)
            }

            scanViewModel.predict.observe(viewLifecycleOwner) { analyzeResult ->
                if (!analyzeResult.error) {
                    val toResult = PreviewFragmentDirections.actionNavigationPreviewToNavigationResult()
                    toResult.image = analyzeResult.imageUrl
                    toResult.predictedClass = analyzeResult.predictedClass
                    toResult.recommendations = Gson().toJson(analyzeResult.recommendations, Recommendations::class.java)
                    findNavController().navigate(toResult)
                }
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            binding?.btnAnalyze?.text = ""
            binding?.loader?.visibility = View.VISIBLE
        } else {
            binding?.btnAnalyze?.text = getString(R.string.analyze)
            binding?.loader?.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}