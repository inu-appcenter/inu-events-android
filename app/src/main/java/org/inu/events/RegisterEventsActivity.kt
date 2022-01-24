package org.inu.events

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import org.inu.events.databinding.RegisterEventsBinding
import org.inu.events.viewmodel.RegisterEventsViewModel
import java.util.*

class RegisterEventsActivity : AppCompatActivity() {
    companion object {
        private const val PERMISSION_ALBUM = 101
    }

    private val viewModel: RegisterEventsViewModel by viewModels()
    private lateinit var binding: RegisterEventsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.register_events)
        binding.registerViewModel = viewModel
        binding.lifecycleOwner = this

        setupToolbar()
        setupButtons()
        setupCurrentDate()
        setupStartDatePicker()
        setupStartTimePicker()
        setupEndDatePicker()
        setupEndTimePicker()
        initAddPhotoButton()
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
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)
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
                cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false
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
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)
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
                cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false
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

        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode != Activity.RESULT_OK) {
                return@registerForActivityResult
            }

            it.data?.data?.let { uri ->

                //imageview.setImageURI(uri)
            } ?: Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
        }

        launcher.launch(intent)
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
}
