package org.inu.events

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.DataBindingUtil
import org.inu.events.common.db.SharedPreferenceWrapper
import org.inu.events.common.extension.getIntExtra
import org.inu.events.common.extension.observe
import org.inu.events.common.extension.observeNonNull
import org.inu.events.common.extension.toast
import org.inu.events.data.model.dto.AlarmDisplayModel
import org.inu.events.databinding.ActivityDetailBinding
import org.inu.events.dialog.AlarmDialog
import org.inu.events.googlelogin.GoogleLoginWrapper
import org.inu.events.objects.IntentMessage.BACK_FROM_ALARM
import org.inu.events.objects.IntentMessage.EVENT_ID
import org.inu.events.service.AlarmReceiver
import org.inu.events.service.LoginService
import org.inu.events.viewmodel.DetailViewModel
import org.koin.android.ext.android.inject
import java.util.*

class DetailActivity : AppCompatActivity() {
    companion object {
        fun callingIntent(context: Context, eventId: Int = -1) =
            Intent(context, DetailActivity::class.java).apply {
                putExtra(EVENT_ID, eventId)
            }

        private const val SHARED_PREFERENCES_NAME = "alarm"
    }

    // 전역 변수로 변경
    private var id: Int = -1

    private val loginService: LoginService by inject()
    private val viewModel: DetailViewModel by viewModels()
    private lateinit var binding: ActivityDetailBinding

    private val googleLogin = GoogleLoginWrapper(this)
    private var backFromAlarm = false

    private val dialog = AlarmDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()

        initCommentButton()
        initOnOffButton()

        setupToolbar()
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        binding.detailViewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun initCommentButton() {
        observeNonNull(viewModel.commentClickEvent) {
            startActivity(CommentActivity.callingIntent(this, it))
        }
    }

    // onOffButton clickEvent
    private fun initOnOffButton() {
        observe(viewModel.alarmClickEvent) {
            val model = binding.onOffButton.tag as? AlarmDisplayModel ?: return@observe
            val newModel = saveAlarmModel(model.onOff.not())
            renderOnOffButton(newModel)
            if (newModel.onOff) {
                dialog.showDialog(this,getString(R.string.alarm_on_title),getString(R.string.alarm_on_content))
                // On -> 알람 등록
                val calendar = Calendar.getInstance().apply {
                    // todo 시간 알맞게
                    val from = "2022-02-19 5:45:00"
                    time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).parse(from)
                }
                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(this, AlarmReceiver::class.java).apply {
                    putExtra("title to receiver", binding.detailTitle.text)
                    putExtra("eventId to receiver", id)
                }
                val pendingIntent = PendingIntent.getBroadcast(
                    // 이렇게하면 계속 쌓이기에 ONOFF_KEY 로 하면 각 eventId에 맞게 업데이트
                    this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT
                )
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                dialog.showDialog(this,getString(R.string.alarm_off_title),getString(R.string.alarm_off_content))
                // Off -> 알람 제거
                cancelAlarm()
            }
        }
    }

    // 각 게시글마다 저장된 onOff fetch
    private fun fetchDataFromSharedPreferences(): AlarmDisplayModel {
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val onOffDBValue = sharedPreferences.getBoolean(id.toString(), false)
        val alarmModel = AlarmDisplayModel(
            onOff = onOffDBValue
        )
//         보정 예외처리
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            id,
            Intent(this, AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE
        )

        if ((pendingIntent == null) and alarmModel.onOff) {
            // 알람은 꺼져있는데 데이터는 켜져있는 경우
            alarmModel.onOff = false
        } else if ((pendingIntent != null) and alarmModel.onOff.not()) {
            // 알람은 커져있는데 데이터는 꺼져있는 경우
            // 알람 취소
            pendingIntent.cancel()
        }
        return alarmModel
    }

    // 알람버튼 클릭될때마다 onOff 저장
    private fun saveAlarmModel(onOff: Boolean): AlarmDisplayModel {
        val model = AlarmDisplayModel(
            onOff = onOff
        )
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean(id.toString(), model.onOff)
            commit()
        }

        return model
    }

    // 알람 취소
    private fun cancelAlarm() {
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            id,
            Intent(this, AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE
        )
        pendingIntent?.cancel()
    }

    // 알람 버튼 요소 변경
    private fun renderOnOffButton(model: AlarmDisplayModel) {
        viewModel.loadOnOffButton(onOff = model.onOff)
        binding.onOffButton.tag = model
    }

    private fun setupToolbar() {
        binding.detailToolbar.toolbarImageView.setOnClickListener { isFromAlarm() }
        //todo - 툴바메뉴는 자신이 작성한 글일 경우에만 노출돼야함
        if (loginService.isLoggedIn) {
            if (viewModel.isMyWriting()) {
                setSupportActionBar(binding.detailToolbar.toolbarRegister)
                supportActionBar?.setDisplayShowTitleEnabled(false)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.event_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.fixToolbarMenu -> {
                Log.d("tag", "fixToolbarMenu menu clicked!")
                startActivity(RegisterEventsActivity.callingIntent(this, viewModel.eventIndex))
                true
            }
            R.id.deleteToolbarMenu -> {
                Log.d("tag", "deleteToolbarMenu menu clicked!")
                true
            }
            R.id.signOutToolbarMenu -> {
                googleLogin.signOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu!!.findItem(R.id.signOutToolbarMenu).isEnabled = loginService.isLoggedIn
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onStart() {
        super.onStart()

        extractEventIdAndLoad()

        val model = fetchDataFromSharedPreferences()
        renderOnOffButton(model)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        isFromAlarm()
    }

    private fun extractEventIdAndLoad() {
        id = getIntExtra(EVENT_ID) ?: return
        viewModel.load(id)
    }

    // 이 액티비티가 알람에서 왔다면 뒤로가기 처리를 해주세요~
    private fun isFromAlarm(){
        backFromAlarm = intent.getBooleanExtra(BACK_FROM_ALARM,false)
        if (backFromAlarm) {

            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        else{
            finish()
        }
    }
}