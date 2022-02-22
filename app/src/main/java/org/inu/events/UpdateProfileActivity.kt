package org.inu.events

import android.R.attr
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Base64.NO_WRAP
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.net.toFile
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.inu.events.base.BaseActivity
import org.inu.events.databinding.ActivityUpdateProfileBinding
import org.inu.events.viewmodel.UpdateProfileViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.lang.Exception


class UpdateProfileActivity : BaseActivity<ActivityUpdateProfileBinding>() {
    override val layoutResourceId = R.layout.activity_update_profile
    private val viewModel: UpdateProfileViewModel by viewModels()
    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            viewModel.inputText.value = s.toString()
            viewModel.validateNickname()
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            try {
                val uri = result.data?.data
                Glide.with(this).load(uri).into(binding.photoUpdate)

                val file = uri!!.toFile()
                val requestFile = file.asRequestBody("multipart/form-data".toMediaType())
                val image = MultipartBody.Part.createFormData("file", file.name, requestFile)
                viewModel.uploadImage(image)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun dataBinding() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.nicknameUpdate.addTextChangedListener(textWatcher)
        viewModel.finishEvent.observe(this) {
            if(viewModel.validateNickname()) finish()
        }

        viewModel.updatePhotoEvent.observe(this) {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            launcher.launch(intent)
        }
    }
}