package org.inu.events

import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import org.inu.events.base.BaseActivity
import org.inu.events.common.extension.asRequestBody
import org.inu.events.common.extension.toast
import org.inu.events.databinding.ActivityUpdateProfileBinding
import org.inu.events.lib.actionsheet.UniActionSheet
import org.inu.events.viewmodel.UpdateProfileViewModel


class UpdateProfileActivity : BaseActivity<ActivityUpdateProfileBinding>() {
    override val layoutResourceId = R.layout.activity_update_profile
    private val viewModel: UpdateProfileViewModel by viewModels()

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                try {
                    val uri = result.data?.data
                    if (uri == null) {
                        toast("이미지를 가져오지 못 하였습니다 ㅠㅡㅠ")
                        return@registerForActivityResult
                    }

                    Glide.with(this).load(uri).into(binding.photoUpdate)

                    val stream = contentResolver.openInputStream(uri)
                    if (stream == null) {
                        toast("입력 스트림을 열지 못 하였습니다 ㅠㅡㅠ")
                        return@registerForActivityResult
                    }

                    val requestFile = stream.asRequestBody("multipart/form-data".toMediaType())
                    val image = MultipartBody.Part.createFormData("file", "file", requestFile)
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