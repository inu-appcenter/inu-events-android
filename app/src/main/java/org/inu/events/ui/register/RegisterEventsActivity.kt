package org.inu.events.ui.register

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import org.inu.events.R
import org.inu.events.common.extension.getIntExtra
import org.inu.events.common.extension.observe
import org.inu.events.common.extension.registerForActivityResult
import org.inu.events.common.extension.toast
import org.inu.events.common.util.URIPathHelper
import org.inu.events.databinding.ActivityRegisterEventsBinding
import org.inu.events.objects.IntentMessage.EVENT_ID
import java.util.*

class RegisterEventsActivity : AppCompatActivity() {
    companion object {
        private const val PERMISSION_ALBUM = 101

        fun callingIntent(context: Context, eventId: Int = -1) =
            Intent(context, RegisterEventsActivity::class.java).apply {
                putExtra(EVENT_ID, eventId)
            }
    }

    private val viewModel: RegisterEventsViewModel by viewModels()
    private lateinit var binding: ActivityRegisterEventsBinding

    private val selectImageLauncher = registerForActivityResult {
        it.takeIf { it.resultCode == Activity.RESULT_OK }?.data?.data?.let { uri ->
            val uriPathHelper = URIPathHelper()
            val filePath = uriPathHelper.getPath(this, uri)
            viewModel.imageUrl.value = filePath
            viewModel.updateImage()
        }?: Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
    }

    private val titleTextWatcher = object: TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            viewModel.titleEditTextEmpty.value = s.toString().isEmpty()
        }
        override fun afterTextChanged(s: Editable?) { }
    }
    private val targetTextWatcher = object: TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            viewModel.targetEditTextEmpty.value = s.toString().isEmpty()
        }
        override fun afterTextChanged(s: Editable?) { }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        setupToolbar()
        setupCancelButtons()
        setupCurrentDate()
        setupStartDatePicker()
        setupStartTimePicker()
        setupEndDatePicker()
        setupEndTimePicker()
        initAddPhotoButton()
        addEvent()
        extractEventIdAndLoad()
    }


    private fun addEvent() {
        viewModel.previewEvent.observe(
            this
        ) {
            when {
                viewModel.period.startTimeEndTime(viewModel.startAt,viewModel.endAt) -> {
                    toast("올바른 행사 마감을 설정해 주세요.")
                    viewModel.onBeforeClick()
                }
                viewModel.isRequiredInformationEntered() -> viewModel.onNextClick()
                else -> {
                    Toast.makeText(this,"필수정보를 모두 입력해주세요",Toast.LENGTH_SHORT).show()
                    setUpTextWatcher()
                    viewModel.onBeforeClick()
                }
            }
        }
        viewModel.finishEvent.observe(
            this
        ) {
            finish()
        }
    }

    private fun setUpTextWatcher() {
        binding.editTextTitle.addTextChangedListener(titleTextWatcher)
        binding.editTextTarget.addTextChangedListener(targetTextWatcher)
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_events)
        binding.registerViewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun setupCurrentDate() {
        viewModel.setupCurrentTime()

        with(binding) {
            editTextStartDate.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            editTextStartTime.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            editTextEndDate.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            editTextEndTime.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        }
    }

    private fun setupCancelButtons() {
        observe(viewModel.startHomeActivityClickEvent) {
            finish()
        }
    }

    private fun setupStartDatePicker() {
        viewModel.startDatePickerClickEvent.observe(this) {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, y, m, d ->
                    cal.set(y, m, d)
                    viewModel.period.setStartDate(cal.time)
                    viewModel.datePickerValueStartYear = y
                    viewModel.datePickerValueStartMonth = m + 1
                    viewModel.datePickerValueStartDay = d
                },
                viewModel.datePickerValueStartYear,
                viewModel.datePickerValueStartMonth - 1,
                viewModel.datePickerValueStartDay
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
                    viewModel.period.setStartTime(cal.time)
                    viewModel.timePickerValueStartTime = h
                    viewModel.timePickerValueStartMinute = m
                },
                viewModel.timePickerValueStartTime, viewModel.timePickerValueStartMinute, false
            ).show()
        }
    }

    private fun setupEndDatePicker() {
        viewModel.endDatePickerClickEvent.observe(this) {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, y, m, d ->
                    cal.set(y, m, d)
                    viewModel.period.setEndDate(cal.time)
                },
                viewModel.datePickerValueEndYear,
                viewModel.datePickerValueEndMonth - 1,
                viewModel.datePickerValueEndDay
            ).apply {
                datePicker.minDate = cal.apply {
                    cal.set(
                        viewModel.datePickerValueStartYear,
                        viewModel.datePickerValueStartMonth - 1,
                        viewModel.datePickerValueStartDay
                    )
                }.timeInMillis
            }.show()
        }
    }

    private fun setupEndTimePicker() {
        viewModel.endTimePickerClickEvent.observe(this) {
            val cal = Calendar.getInstance()
            TimePickerDialog(
                this, { _, h, m ->
                    cal.set(Calendar.HOUR_OF_DAY, h)
                    cal.set(Calendar.MINUTE, m)
                    viewModel.period.setEndTime(cal.time)
                    viewModel.timePickerValueEndTime = h
                    viewModel.timePickerValueEndMinute = m
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
        binding.toolbarRegister0.setOnBackListener { finish() }
        binding.toolbarRegister1.setOnBackListener { finish() }
        binding.toolbarRegister2.setOnBackListener { finish() }
        binding.toolbarRegister3.setOnBackListener { finish() }
    }

    private fun extractEventIdAndLoad() {
        val id = getIntExtra(EVENT_ID) ?: -1

        viewModel.load(id)
    }
}
