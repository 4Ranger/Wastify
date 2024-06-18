package com.capstonewahwah.wastify.ui.main.classifications.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.capstonewahwah.wastify.R
import com.capstonewahwah.wastify.databinding.FragmentClassificationDetailsBinding

class ClassificationDetailsFragment : Fragment() {

    private var _binding: FragmentClassificationDetailsBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentClassificationDetailsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = ClassificationDetailsFragmentArgs.fromBundle(arguments as Bundle)

        Glide.with(requireContext())
            .load(data.image)
            .into(binding?.ivClassification!!)
        binding?.tvClassifications?.text = data.classification
        binding?.tvRot?.text = getString(R.string.rot, data.rot)
        binding?.tvReuse?.text = getString(R.string.reuse, data.reuse)
        binding?.tvReduce?.text = getString(R.string.reduce, data.reduce)
        binding?.tvRefuse?.text = getString(R.string.refuse, data.refuse)
        binding?.tvRecycle?.text = getString(R.string.recycle, data.recycle)
        binding?.tvRepurpose?.text = getString(R.string.repurpose, data.repurpose)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}