package com.fouranger.wastify.ui.main.home.profile

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
import androidx.fragment.app.viewModels
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.fouranger.wastify.R
import com.fouranger.wastify.databinding.FragmentProfileBinding
import com.fouranger.wastify.helper.Utils.reduceFileImage
import com.fouranger.wastify.helper.Utils.uriToFile
import com.fouranger.wastify.helper.ViewModelFactory
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
        binding?.tvEmail?.text = data.email

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
            croppedImageUri?.let { uri ->
                val imageFile = uriToFile(uri, requireContext()).reduceFileImage()
                val imageRequestBody = imageFile.asRequestBody("image/jpeg".toMediaType())
                val imageMultipartBody = MultipartBody.Part.createFormData(
                    "file",
                    imageFile.name,
                    imageRequestBody
                )
                profileViewModel.updateUser(data.token, null, null, imageMultipartBody)
            }
        }

        profileViewModel.edtIsLoading.observe(viewLifecycleOwner) { isLoading ->
            setLoading(isLoading)
            setLoadingForUsername(isLoading)
            setLoadingForEmail(isLoading)
        }

        // username
        binding?.btnChangeUsername?.setOnClickListener {
            binding?.llUsername?.visibility = View.GONE
            binding?.llChangeUsr?.visibility = View.VISIBLE
        }

        binding?.btnCancelUsername?.setOnClickListener {
            binding?.llUsername?.visibility = View.VISIBLE
            binding?.llChangeUsr?.visibility = View.GONE
        }

        binding?.btnOkUsername?.setOnClickListener {
            val newUsername = binding?.edtNewUsername?.text?.toString()?.trim()

            if (newUsername?.isEmpty()!!) binding?.edtNewUsername?.error = getString(R.string.please_insert_your_new_username_first)
            else {
                val usernameRequestBody = newUsername.toRequestBody("text/plain".toMediaType())
                binding?.tvUsername?.text = newUsername
                profileViewModel.updateUser(data.token, null, usernameRequestBody, null)
            }
        }

        // email
        binding?.btnChangeEmail?.setOnClickListener {
            binding?.llEmail?.visibility = View.GONE
            binding?.llChangeEmail?.visibility = View.VISIBLE
        }

        binding?.btnCancelEmail?.setOnClickListener {
            binding?.llEmail?.visibility = View.VISIBLE
            binding?.llChangeEmail?.visibility = View.GONE
        }

        binding?.btnOkEmail?.setOnClickListener {
            val newEmail = binding?.edtNewEmail?.text?.toString()?.trim()

            if (newEmail?.isEmpty()!!) binding?.edtNewEmail?.error = getString(R.string.please_insert_your_new_email_first)
            else {
                val emailRequestBody = newEmail.toRequestBody("text/plain".toMediaType())
                binding?.tvEmail?.text = newEmail
                profileViewModel.updateUser(data.token, emailRequestBody, null, null)
            }
        }

        profileViewModel.updatedUser.observe(viewLifecycleOwner) { response ->
            if (response.message == "Profile updated successfully" && response.url == null) {
                binding?.llChangeUsr?.visibility = View.GONE
                binding?.llChangeEmail?.visibility = View.GONE
                binding?.llUsername?.visibility = View.VISIBLE
                binding?.llEmail?.visibility = View.VISIBLE
            } else {
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                binding?.btnSave?.visibility = View.GONE
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

    private fun setLoadingForUsername(isLoading: Boolean) {
        if (isLoading) {
            binding?.btnOkUsername?.visibility = View.GONE
            binding?.loaderuser?.visibility = View.VISIBLE
        } else {
            binding?.btnOkUsername?.visibility = View.VISIBLE
            binding?.loaderuser?.visibility = View.GONE
        }
    }

    private fun setLoadingForEmail(isLoading: Boolean) {
        if (isLoading) {
            binding?.btnOkEmail?.visibility = View.GONE
            binding?.loaderemail?.visibility = View.VISIBLE
        } else {
            binding?.btnOkEmail?.visibility = View.VISIBLE
            binding?.loaderemail?.visibility = View.GONE
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