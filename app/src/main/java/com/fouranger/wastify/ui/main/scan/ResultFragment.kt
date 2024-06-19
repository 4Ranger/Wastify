package com.fouranger.wastify.ui.main.scan

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.transition.Slide
import com.bumptech.glide.Glide
import com.fouranger.wastify.R
import com.fouranger.wastify.data.remote.response.Recommendations
import com.fouranger.wastify.databinding.FragmentResultBinding
import com.google.gson.Gson

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = Slide(Gravity.END)
        exitTransition = Slide(Gravity.TOP)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val result = ResultFragmentArgs.fromBundle(arguments as Bundle)
        val recommendations: Recommendations = Gson().fromJson(result.recommendations, Recommendations::class.java)

        Glide.with(requireContext())
            .load(result.image)
            .into(binding?.ivResult!!)

        binding?.tvMadeFrom?.text = getString(R.string.made_from, result.predictedClass)
        binding?.tvRecommendation?.text = getString(R.string.recommendation, recommendations.recycle)

        binding?.btnHome?.setOnClickListener {
            val toHome = ResultFragmentDirections.actionNavigationResultToNavigationHome()
            findNavController().navigate(toHome)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}