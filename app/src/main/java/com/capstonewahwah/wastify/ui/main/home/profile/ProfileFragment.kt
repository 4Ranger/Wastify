package com.capstonewahwah.wastify.ui.main.home.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.capstonewahwah.wastify.R
import com.capstonewahwah.wastify.databinding.FragmentProfileBinding
import com.capstonewahwah.wastify.helper.Utils.reduceFileImage
import com.capstonewahwah.wastify.helper.Utils.uriToFile
import com.capstonewahwah.wastify.helper.ViewModelFactory
import com.capstonewahwah.wastify.ui.main.MainViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.yalantis.ucrop.UCrop
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import kotlin.random.Random


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding

    private var currentImageUri: Uri? = null
    private var croppedImageUri: Uri? = null

    private val profileViewModel by viewModels<ProfileViewModel> {
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

        Glide.with(requireContext())
            .load(data.photoUrl)
            .into(binding?.ivUser!!)

        binding?.tvUsername?.text = data.username

        binding?.cvProfilePicture?.setOnClickListener {
            launchGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding?.btnChangePwd?.setOnClickListener {
            val customLayout = layoutInflater.inflate(R.layout.change_pwd_layout, null)
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
                .setView(customLayout)

            val changepwdBtn = customLayout.findViewById<MaterialButton>(R.id.btn_change)
            val edtOldPwd = customLayout.findViewById<TextInputEditText>(R.id.edt_old_pwd)
            val edtNewPwd = customLayout.findViewById<TextInputEditText>(R.id.edt_new_pwd)
            val loader = customLayout.findViewById<ProgressBar>(R.id.loader)

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()

            changepwdBtn.setOnClickListener {
                if (edtOldPwd.text.toString().trim().isEmpty()) {
                    edtOldPwd.error = "Masukkan password lama anda terlebih dahulu"
                } else if (edtNewPwd.text.toString().trim().isEmpty()) {
                    edtNewPwd.error = "Masukkan password baru anda"
                } else {
                    profileViewModel.changePwd(
                        data.token,
                        data.email,
                        edtOldPwd.text.toString().trim(),
                        edtNewPwd.text.toString().trim()
                    )
                }

                fun setLoading(isLoading: Boolean) {
                    if (isLoading) {
                        changepwdBtn.text = ""
                        loader.visibility = View.VISIBLE
                    } else {
                        changepwdBtn.text = getString(R.string.change)
                        loader.visibility = View.GONE
                    }
                }

                profileViewModel.pwdIsLoading.observe(viewLifecycleOwner) { isLoading ->
                    setLoading(isLoading)
                    if (!isLoading) {
                        alertDialog.dismiss()
                    }
                }

                profileViewModel.pwdChange.observe(viewLifecycleOwner) { response ->
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding?.btnSave?.setOnClickListener {
            currentImageUri?.let { uri ->
                val imageFile = uriToFile(uri, requireContext()).reduceFileImage()
                val usernameRequestBody = data.username.toRequestBody("text/plain".toMediaType())
                val emailRequestBody = data.email.toRequestBody("text/plain".toMediaType())
                val imageRequestBody = imageFile.asRequestBody("image/jpeg".toMediaType())
                val multipartBody = MultipartBody.Part.createFormData(
                    "file",
                    imageFile.name,
                    imageRequestBody
                )
                profileViewModel.editProfile(data.token, usernameRequestBody, emailRequestBody, multipartBody)
            }
            profileViewModel.editedProfile.observe(viewLifecycleOwner) { response ->
                if (response.message == "Profile updated successfully"){
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                    binding?.btnSave?.visibility = View.GONE
                }
            }

            profileViewModel.edtIsLoading.observe(viewLifecycleOwner) { isLoading ->
                setLoading(isLoading)
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            binding?.btnSave?.text = ""
            binding?.loader?.visibility = View.VISIBLE
        } else {
            binding?.btnSave?.text = getString(R.string.save)
            binding?.loader?.visibility = View.GONE
        }
    }

    private val ucropContracts = object : ActivityResultContract<List<Uri>, Uri>() {
        override fun createIntent(context: Context, input: List<Uri>): Intent {
            val inputUri = input[0]
            val outputUri = input[1]

            val ucrop = UCrop.of(inputUri, outputUri)
                .withAspectRatio(5f, 5f)
                .withMaxResultSize(800, 800)
            return ucrop.getIntent(context)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri {
            return if (intent != null && resultCode == Activity.RESULT_OK) {
                UCrop.getOutput(intent)!!
            } else {
                Uri.EMPTY
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

    private val cropImage = registerForActivityResult(ucropContracts) { uri ->
        binding?.ivUser?.setImageURI(uri)
        croppedImageUri = uri
    }

    private fun showImage() {
        val imageId = Random(10)
        currentImageUri?.let {
            val inputUri = it
            val outputUri = File(context?.filesDir, "croppedImage$imageId.png").toUri()
            val listUri = listOf(inputUri, outputUri)
            cropImage.launch(listUri)

            binding?.btnSave?.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}