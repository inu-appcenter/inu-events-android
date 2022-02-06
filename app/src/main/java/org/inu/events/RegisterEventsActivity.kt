package org.inu.events

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import org.inu.events.data.model.Article
import org.inu.events.databinding.RegisterEventsBinding
import org.inu.events.objects.IntentMessage
import org.inu.events.viewmodel.RegisterEventsViewModel
import java.util.*


class RegisterEventsActivity : AppCompatActivity() {
    companion object {
        private const val PERMISSION_ALBUM = 101
    }

    private val viewModel: RegisterEventsViewModel by viewModels()
    private lateinit var binding: RegisterEventsBinding

    private val selectImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode != Activity.RESULT_OK) {
                return@registerForActivityResult
            }

            it.data?.data?.let { uri ->
                viewModel.onImageSelected(uri)
                Log.d("tag","$uri")
            } ?: Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        setupToolbar()
        setupButtons()
        setupCurrentDate()
        setupStartDatePicker()
        setupStartTimePicker()
        setupEndDatePicker()
        setupEndTimePicker()
        initAddPhotoButton()
        addEvent()
        getEventId()

        Log.d("tag","${viewModel.eventIndex.value}")
        Log.d("tag","${viewModel.detailDataList.value?.title}")
    }

    private fun addEvent() {
        viewModel.completeButtonClickEvent.observe(
            this, {
                startActivity(Intent(this, MainActivity::class.java))
            }
        )
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.register_events)
        binding.registerViewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun setupCurrentDate() {
        val date = Date()

        with(viewModel) {
            setStartDate(date)
            setStartTime(date)
            setEndDate(date)
            setEndTime(date)
        }

        with(binding) {
            editTextStartDatePeriod.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            editTextStartTimePeriod.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            editTextEndDatePeriod.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            editTextEndTimePeriod.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        }
    }

    private fun setupButtons() {
        viewModel.startHomeActivityClickEvent.observe(
            this, {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        )
    }

    private fun setupStartDatePicker() {
        viewModel.startDatePickerClickEvent.observe(this) {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                this, { _, y, m, d ->
                    cal.set(y, m, d)
                    viewModel.setStartDate(cal.time)
                },
                viewModel.datePickerValueStartYear, viewModel.datePickerValueStartMonth-1, viewModel.datePickerValueStartDay
            ).show()

        }
    }

    private fun setupStartTimePicker() {
        viewModel.startTimePickerClickEvent.observe(this) {
            val cal = Calendar.getInstance()
            TimePickerDialog(
                this, { _, h, m ->
                    cal.set(Calendar.HOUR_OF_DAY, h)
                    cal.set(Calendar.MINUTE, m)
                    viewModel.setStartTime(cal.time)
                },
                viewModel.timePickerValueStartTime, viewModel.timePickerValueStartMinute, false
            ).show()
        }
    }

    private fun setupEndDatePicker() {
        viewModel.endDatePickerClickEvent.observe(this) {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                this, { _, y, m, d ->
                    cal.set(y, m, d)
                    viewModel.setEndDate(cal.time)
                },
                viewModel.datePickerValueEndYear, viewModel.datePickerValueEndMonth-1, viewModel.datePickerValueEndDay
            ).show()
        }
    }

    private fun setupEndTimePicker() {
        viewModel.endTimePickerClickEvent.observe(this) {
            val cal = Calendar.getInstance()
            TimePickerDialog(
                this, { _, h, m ->
                    cal.set(Calendar.HOUR_OF_DAY, h)
                    cal.set(Calendar.MINUTE, m)
                    viewModel.setEndTime(cal.time)
                },
                viewModel.timePickerValueEndTime, viewModel.timePickerValueEndMinute, false
            ).show()
        }
    }

    private fun initAddPhotoButton() {
        viewModel.startGalleryClickEvent.observe(this) {
            Log.i("BUTTON", "initAddPhotoButton")

            val permissionStatus = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )

            when (permissionStatus) {
                PackageManager.PERMISSION_GRANTED -> {
                    navigatePhotos()
                    Log.i("BUTTON", "gogogogogogogogo")
                }

                PackageManager.PERMISSION_DENIED -> {
                    val userDenied = shouldShowRequestPermissionRationale(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    )

                    if (userDenied) {
                        Log.i("BUTTON", "user denied :(")

                        showPermissionContextPopup()
                    } else {
                        Log.i("BUTTON", "request permission!")

                        requestPermissions(
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                            PERMISSION_ALBUM
                        )
                    }
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
            PERMISSION_ALBUM -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // todo 권한이 부여된 것입니다.
                    navigatePhotos()
                } else {
                    Toast.makeText(this, "권한을 거부하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                Log.wtf("OOOOH", "what is this?")
            }
        }
    }

    private fun navigatePhotos() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        selectImageLauncher.launch(intent)
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
            if(id == -1) viewModel.check.value = 1
            else{
                Log.d("tag","게시글의 id는 $id")
                viewModel.eventIndex = MutableLiveData(id)
            }
        }
    }

}
