package com.example.sbaby.task

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.example.sbaby.R
import com.example.sbaby.databinding.CardEditProfileBinding


class EditChildInfoDialogFragment : DialogFragment() {
    private val binding: CardEditProfileBinding by viewBinding()
    private var name: String? = null
    private var photo: String? = null

    companion object {
        const val TAG = "EditChildInfoDialogFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getDialog()?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.card_edit_profile, container)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            val mArgs = arguments
            name = mArgs?.getString("name")
            photo = mArgs?.getString("photo")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nameEditText.text = SpannableStringBuilder(name)
        binding.profileImage.load(photo)
        binding.galleryPhotoButton.setOnClickListener { _ ->
            loadPhotoFromGallery()
        }
        binding.takePhotoButton.setOnClickListener { _ ->
            takePhoto()
        }
        binding.okButton.setOnClickListener { _ ->

            this.dismiss()
        }
        binding.cancelButton.setOnClickListener { _ ->
            this.dismiss()
        }
    }

    private fun takePhoto() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, 1)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    private fun loadPhotoFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            try {
                val imageBitmap = data?.extras?.get("data") as Bitmap
                binding.profileImage.setImageBitmap(imageBitmap)
            } catch (e: Exception) {
            }
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            try {
                val selectedImageUri: Uri = data?.getData()!!
                Log.e("hhhh", selectedImageUri.toString())
                binding.profileImage.setImageURI(selectedImageUri)
            } catch (e: Exception) {
            }
        }
    }

}