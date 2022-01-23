package org.inu.events

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import org.inu.events.databinding.RegisterEventsBinding
import org.inu.events.objects.EventNumber.EVENT_START_GALLERY
import org.inu.events.objects.EventNumber.EVENT_START_MAIN_ACTIVITY
import org.inu.events.objects.IntentMessage
import org.inu.events.viewmodel.DetailViewModel
import org.inu.events.viewmodel.RegisterEventsViewModel

class RegisterEventsActivity : AppCompatActivity() {
    // todo : 수연 - 이후 다른 파일로 빼기
    private val PERMISSION_ALBUM = 101
    private val REQUEST_STORAGE = 1000

    private lateinit var registerModel: RegisterEventsViewModel
    private lateinit var binding: RegisterEventsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        setupToolbar()
        setupButtons()
        initAddPhotoButton()
        getEventId()
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.register_events)
        registerModel = RegisterEventsViewModel()
        binding.registerViewModel = registerModel
        binding.lifecycleOwner = this
    }

    private fun setupButtons() {
        registerModel.startHomeActivityClickEvent.observe(
            this, {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        )
    }

    private fun initAddPhotoButton() {
        registerModel.startGalleryClickEvent.observe(this, {
            Log.i("BUTTON", "initAdd")
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    navigatePhotos()
                    Log.i("BUTTON", "navigate")
                }
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    showPermissionContextPopup()
                    Log.i("BUTTON", "showpermission")
                }
                else -> {
                    requestPermissions(
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        REQUEST_STORAGE
                    )
                    Log.i("BUTTON", "else")
                }
            }

        }
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        when (requestCode) {
            REQUEST_STORAGE -> {
                val selectedImageUri: Uri? = data?.data
                if (selectedImageUri != null) {
                    selectedImageUri?.let { uri ->
                        //imageview.setImageURI(uri)
                    }

                }else{
                    Toast.makeText(this,"사진을 가져오지 못했습니다.",Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // todo 권한이 부여된 것입니다.
                    navigatePhotos()
                } else {
                    Toast.makeText(this, "권한을 거부하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {

            }
        }
    }

    private fun navigatePhotos() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_STORAGE)
    }

    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("앱에서 사진을 불러오기 위해 권한이 필요합니다.")
            .setPositiveButton("동의") { _, _ ->
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_ALBUM
                )
            }
            .setNegativeButton("취소") { _, _ -> }
            .create()
            .show()
    }

    private fun setupToolbar() {
        binding.toolbarRegister1.toolbarImageView.setOnClickListener { finish() }
        binding.toolbarRegister2.toolbarImageView.setOnClickListener { finish() }
    }

    private fun getEventId() {
        val extras = intent.extras?:null
        if(intent.hasExtra(IntentMessage.POST_EDIT_INFO)){
            var id:Int? = extras?.getInt(IntentMessage.POST_EDIT_INFO)
            Log.d("tag","게시글의 id는 $id")
            registerModel.eventIndex = MutableLiveData(id)
        }
    }

}
