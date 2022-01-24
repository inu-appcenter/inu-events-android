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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import org.inu.events.databinding.RegisterEventsBinding
import org.inu.events.viewmodel.RegisterEventsViewModel
import java.text.SimpleDateFormat
import java.util.*

class RegisterEventsActivity : AppCompatActivity() {
    private val PERMISSION_ALBUM = 101
    private val REQUEST_STORAGE = 1000

    private lateinit var registerModel: RegisterEventsViewModel
    private lateinit var binding: RegisterEventsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.register_events)
        registerModel = RegisterEventsViewModel()
        binding.registerViewModel = registerModel
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
        val now: Long = System.currentTimeMillis()
        val formatDate = SimpleDateFormat("yyyy.MM.dd", Locale("ko", "KR")).format(now).toString()
        val formatTime = SimpleDateFormat("h:mm a", Locale("en", "US")).format(now).toString()
        registerModel.start_date_period.value = formatDate
        registerModel.start_time_period.value = formatTime
        registerModel.end_date_period.value = formatDate
        registerModel.end_time_period.value = formatTime
        binding.editTextStartDatePeriod.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        binding.editTextStartTimePeriod.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        binding.editTextEndDatePeriod.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        binding.editTextEndTimePeriod.paintFlags = Paint.UNDERLINE_TEXT_FLAG
    }

    private fun setupButtons() {
        registerModel.startHomeActivityClickEvent.observe(
            this, {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        )
    }

    private fun setupStartDatePicker() {
        registerModel.startDatePickerClickEvent.observe(this, {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                this, DatePickerDialog.OnDateSetListener { datePicker, y, m, d ->
                    cal.set(y, m, d)
                    registerModel.start_date_period.value =
                        SimpleDateFormat("yyyy.MM.dd", Locale("ko", "KR")).format(cal.time)
                            .toString()
                },
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)
            ).show()
        })
    }

    private fun setupStartTimePicker() {
        registerModel.startTimePickerClickEvent.observe(this, {
            val cal = Calendar.getInstance()
            TimePickerDialog(
                this, TimePickerDialog.OnTimeSetListener { timePicker, h, m ->
                    cal.set(Calendar.HOUR_OF_DAY, h)
                    cal.set(Calendar.MINUTE, m)
                    registerModel.start_time_period.value =
                        SimpleDateFormat("h:mm a", Locale("en", "US")).format(cal.time).toString()
                },
                cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false
            ).show()
        })
    }

    private fun setupEndDatePicker() {
        registerModel.endDatePickerClickEvent.observe(this, {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                this, DatePickerDialog.OnDateSetListener { datePicker, y, m, d ->
                    cal.set(y, m, d)
                    registerModel.end_date_period.value =
                        SimpleDateFormat("yyyy.MM.dd", Locale("ko", "KR")).format(cal.time)
                            .toString()
                },
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)
            ).show()
        })
    }

    private fun setupEndTimePicker() {
        registerModel.endTimePickerClickEvent.observe(this, {
            val cal = Calendar.getInstance()
            TimePickerDialog(
                this, TimePickerDialog.OnTimeSetListener { timePicker, h, m ->
                    cal.set(Calendar.HOUR_OF_DAY, h)
                    cal.set(Calendar.MINUTE, m)
                    registerModel.end_time_period.value =
                        SimpleDateFormat("h:mm a", Locale("en", "US")).format(cal.time).toString()
                },
                cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false
            ).show()
        })
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
                    selectedImageUri.let { uri ->
                        //imageview.setImageURI(uri)
                    }
                } else {
                    Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
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
}
