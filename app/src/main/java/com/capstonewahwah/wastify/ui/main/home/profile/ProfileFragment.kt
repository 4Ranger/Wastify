package com.capstonewahwah.wastify.ui.main.home.profile

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
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.transition.TransitionInflater
import com.capstonewahwah.wastify.R
import com.capstonewahwah.wastify.databinding.FragmentProfileBinding
import com.capstonewahwah.wastify.helper.Utils.reduceFileImage
import com.capstonewahwah.wastify.helper.Utils.uriToFile
import com.capstonewahwah.wastify.helper.ViewModelFactory
import com.capstonewahwah.wastify.ui.main.MainViewModel
import com.google.android.material.button.MaterialButton
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding

    private var currentImageUri: Uri? = null

    private val profileViewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private val mainViewModel by activityViewModels<MainViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = ProfileFragmentArgs.fromBundle(arguments as Bundle)

        binding?.tvUsername?.text = data.username

        binding?.cvProfilePicture?.setOnClickListener {
            launchGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding?.btnChangePwd?.setOnClickListener {
            val customLayout = layoutInflater.inflate(R.layout.change_pwd_layout, null)
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
                .setView(customLayout)

            val changepwdBtn = customLayout.findViewById<MaterialButton>(R.id.btn_change)

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()

            changepwdBtn.setOnClickListener {
                Toast.makeText(requireContext(), "yay", Toast.LENGTH_SHORT).show()
                alertDialog.dismiss()
            }
        }

        binding?.btnSave?.setOnClickListener {
            currentImageUri?.let { uri ->
                mainViewModel.getSession().observe(viewLifecycleOwner) { user ->
                    val imageFile = uriToFile(uri, requireContext()).reduceFileImage()
                    val usernameRequestBody = user.name.toRequestBody("text/plain".toMediaType())
                    val emailRequestBody = user.email.toRequestBody("text/plain".toMediaType())
                    val imageRequestBody = imageFile.asRequestBody("image/jpeg".toMediaType())
                    val multipartBody = MultipartBody.Part.createFormData(
                        "file",
                        imageFile.name,
                        imageRequestBody
                    )

                    Log.d("profilefragmentoken", user.token)
                    profileViewModel.editProfile(user.token, usernameRequestBody, emailRequestBody, multipartBody)
                }
            }
            profileViewModel.editedProfile.observe(viewLifecycleOwner) { response ->
                if (response.message == "Profile updated successfully")
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val launchGallery = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No Media Selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding?.ivUser?.setImageURI(it)
            binding?.btnSave?.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}