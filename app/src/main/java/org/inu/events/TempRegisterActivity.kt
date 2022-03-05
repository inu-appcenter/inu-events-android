package org.inu.events

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import org.inu.events.adapter.RegisterStateAdapter
import org.inu.events.base.BaseActivity
import org.inu.events.common.extension.getIntExtra
import org.inu.events.common.extension.observe
import org.inu.events.common.extension.registerForActivityResult
import org.inu.events.common.util.URIPathHelper
import org.inu.events.databinding.ActivityTempRegisterBinding
import org.inu.events.objects.IntentMessage
import org.inu.events.viewmodel.TempRegisterViewModel
import java.util.*

class TempRegisterActivity : BaseActivity<ActivityTempRegisterBinding>() {
    companion object {
        private const val PERMISSION_ALBUM = 101

        fun callingIntent(context: Context, eventId: Int = -1) =
            Intent(context, RegisterEventsActivity::class.java).apply {
                putExtra(IntentMessage.EVENT_ID, eventId)
            }
    }

    override val layoutResourceId = R.layout.activity_temp_register
    val registerViewModel: TempRegisterViewModel by viewModels()

    override fun dataBinding() {
        super.dataBinding()
        binding.viewModel = registerViewModel
    }

    override fun afterDataBinding() {
        binding.viewpager.adapter = RegisterStateAdapter(this, registerViewModel)
        binding.viewpager.isUserInputEnabled = false

        registerViewModel.onNextEvent.observe(this) {
            val position = binding.viewpager.currentItem

            if (position + 1 < RegisterStateAdapter.NUMBER_OF_PAGE) binding.viewpager.currentItem = position + 1
            else {
                registerViewModel.onCompleteClick()
//                finish()
            }
        }

        registerViewModel.onPreviousEvent.observe(this) {
            val position = binding.viewpager.currentItem

            if (position == 0) finish()
            else binding.viewpager.currentItem = position - 1
        }
    }

    private val selectImageLauncher = registerForActivityResult {
        it.takeIf { it.resultCode == Activity.RESULT_OK }?.data?.data?.let { uri ->
            val uriPathHelper = URIPathHelper()
            val filePath = uriPathHelper.getPath(this, uri)
            registerViewModel.imageUrl.value = filePath
            registerViewModel.updateImage()
        } ?: Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
    }

    private val titleTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            registerViewModel.titleEditTextEmpty.value = s.toString().isEmpty()
        }

        override fun afterTextChanged(s: Editable?) {}
    }
    private val targetTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            registerViewModel.targetEditTextEmpty.value = s.toString().isEmpty()
        }

        override fun afterTextChanged(s: Editable?) {}
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupCancelButtons()
        setupCurrentDate()
        setupStartDatePicker()
        setupStartTimePicker()
        setupEndDatePicker()
        setupEndTimePicker()
        initAddPhotoButton()
        extractEventIdAndLoad()
    }

    private fun setupCurrentDate() {
        // TODO : 구현해야 함
    }

    private fun setupCancelButtons() {
        observe(registerViewModel.startHomeActivityClickEvent) {
            finish()
        }
    }

    private fun setupStartDatePicker() {
        registerViewModel.startDatePickerClickEvent.observe(this) {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, y, m, d ->
                    cal.set(y, m, d)
                    registerViewModel.setStartDate(cal.time)
                    registerViewModel.datePickerValueStartYear = y
                    registerViewModel.datePickerValueStartMonth = m + 1
                    registerViewModel.datePickerValueStartDay = d
                },
                registerViewModel.datePickerValueStartYear,
                registerViewModel.datePickerValueStartMonth - 1,
                registerViewModel.datePickerValueStartDay
            ).show()

        }
    }

    private fun setupStartTimePicker() {
        registerViewModel.startTimePickerClickEvent.observe(this) {
            val cal = Calendar.getInstance()
            TimePickerDialog(
                this, { _, h, m ->
                    cal.set(Calendar.HOUR_OF_DAY, h)
                    cal.set(Calendar.MINUTE, m)
                    registerViewModel.setStartTime(cal.time)
                    registerViewModel.timePickerValueStartTime = h
                    registerViewModel.timePickerValueStartMinute = m
                },
                registerViewModel.timePickerValueStartTime, registerViewModel.timePickerValueStartMinute, false
            ).show()
        }
    }

    private fun setupEndDatePicker() {
        registerViewModel.endDatePickerClickEvent.observe(this) {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, y, m, d ->
                    cal.set(y, m, d)
                    registerViewModel.setEndDate(cal.time)
                },
                registerViewModel.datePickerValueEndYear,
                registerViewModel.datePickerValueEndMonth - 1,
                registerViewModel.datePickerValueEndDay
            ).apply {
                datePicker.minDate = cal.apply {
                    cal.set(
                        registerViewModel.datePickerValueStartYear,
                        registerViewModel.datePickerValueStartMonth - 1,
                        registerViewModel.datePickerValueStartDay
                    )
                }.timeInMillis
            }.show()
        }
    }

    private fun setupEndTimePicker() {
        registerViewModel.endTimePickerClickEvent.observe(this) {
            val cal = Calendar.getInstance()
            TimePickerDialog(
                this, { _, h, m ->
                    cal.set(Calendar.HOUR_OF_DAY, h)
                    cal.set(Calendar.MINUTE, m)
                    registerViewModel.setEndTime(cal.time)
                    registerViewModel.timePickerValueEndTime = h
                    registerViewModel.timePickerValueEndMinute = m
                },
                registerViewModel.timePickerValueEndTime, registerViewModel.timePickerValueEndMinute, false
            ).show()
        }
    }

    private fun initAddPhotoButton() {
        registerViewModel.startGalleryClickEvent.observe(this) {
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

    private fun extractEventIdAndLoad() {
        val id = getIntExtra(IntentMessage.EVENT_ID) ?: -1

        registerViewModel.load(id)
    }
}