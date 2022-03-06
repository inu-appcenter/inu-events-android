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
    val viewModel: TempRegisterViewModel by viewModels()

    override fun dataBinding() {
        super.dataBinding()
        binding.viewModel = viewModel
    }

    override fun afterDataBinding() {
        binding.viewpager.adapter = RegisterStateAdapter(this, viewModel)
        binding.viewpager.isUserInputEnabled = false

        viewModel.onNextEvent.observe(this) {
            val position = binding.viewpager.currentItem

            if (position + 1 < RegisterStateAdapter.NUMBER_OF_PAGE) binding.viewpager.currentItem = position + 1
            else {
                // TODO : 동기처리 해야함.
                viewModel.onCompleteClick()
                finish()
            }
        }

        viewModel.onPreviousEvent.observe(this) {
            val position = binding.viewpager.currentItem

            if (position == 0) finish()
            else binding.viewpager.currentItem = position - 1
        }

        viewModel.setupCurrentTime()
    }

    private val selectImageLauncher = registerForActivityResult {
        it.takeIf { it.resultCode == Activity.RESULT_OK }?.data?.data?.let { uri ->
            val uriPathHelper = URIPathHelper()
            val filePath = uriPathHelper.getPath(this, uri)
            viewModel.imageUrl.value = filePath
            viewModel.updateImage()
        } ?: Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
    }

    private val titleTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            viewModel.titleEditTextEmpty.value = s.toString().isEmpty()
        }

        override fun afterTextChanged(s: Editable?) {}
    }
    private val targetTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            viewModel.targetEditTextEmpty.value = s.toString().isEmpty()
        }

        override fun afterTextChanged(s: Editable?) {}
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupCancelButtons()
        setupStartDatePicker()
        setupStartTimePicker()
        setupEndDatePicker()
        setupEndTimePicker()
        initAddPhotoButton()
        extractEventIdAndLoad()
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

    private fun extractEventIdAndLoad() {
        val id = getIntExtra(IntentMessage.EVENT_ID) ?: -1

        viewModel.load(id)
    }
}