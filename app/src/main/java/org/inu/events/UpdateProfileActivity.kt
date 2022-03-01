package org.inu.events

import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.inu.events.base.BaseActivity
import org.inu.events.common.util.URIPathHelper
import org.inu.events.databinding.ActivityUpdateProfileBinding
import org.inu.events.lib.actionsheet.UniActionSheet
import org.inu.events.viewmodel.UpdateProfileViewModel
import java.io.File


class UpdateProfileActivity : BaseActivity<ActivityUpdateProfileBinding>() {
    override val layoutResourceId = R.layout.activity_update_profile
    private val viewModel: UpdateProfileViewModel by viewModels()

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                try {
                    val uri = result.data?.data
                    Glide.with(this).load(uri).into(binding.photoUpdate)

                    val filePath = URIPathHelper().getPath(this, uri!!)
                    val file = File(filePath!!)

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

        viewModel.finishEvent.observe(this) {
            if (viewModel.validateNickname()) finish()
        }

        viewModel.updatePhotoEvent.observe(this) {
            UniActionSheet(this)
                .addText("프로필 사진 설정")
                .addAction("앨범에서 사진 선택") {
                    val intent = Intent()
                    intent.type = "image/*"
                    intent.action = Intent.ACTION_GET_CONTENT
                    launcher.launch(intent)
                }
                .addAction("기본 이미지로 변경") {
                    viewModel.resetToDefaultImage()
                }
                .show()
        }
    }

    override fun afterDataBinding() {
        binding.toolbar.setOnBackListener {
            finish()
        }
    }
}