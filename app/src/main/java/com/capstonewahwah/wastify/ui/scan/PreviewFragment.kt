package com.capstonewahwah.wastify.ui.scan

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import androidx.transition.Slide
import com.capstonewahwah.wastify.R
import com.capstonewahwah.wastify.databinding.FragmentPreviewBinding

class PreviewFragment : Fragment() {

    private var _binding: FragmentPreviewBinding? = null
    private val binding get() = _binding

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
        binding?.ivPrev?.setImageURI(image.toUri())

        binding?.btnAnalyze?.setOnClickListener {
            val toResult = PreviewFragmentDirections.actionNavigationPreviewToResultFragment()
            findNavController().navigate(toResult)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}