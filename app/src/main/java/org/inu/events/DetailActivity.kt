package org.inu.events

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.inu.events.common.extension.getIntExtra
import org.inu.events.common.extension.observe
import org.inu.events.common.extension.observeNonNull
import org.inu.events.common.extension.toast
import org.inu.events.databinding.ActivityDetailBinding
import org.inu.events.dialog.AlarmDialog
import org.inu.events.dialog.BottomSheetDialog
import org.inu.events.googlelogin.GoogleLoginWrapper
import org.inu.events.objects.IntentMessage.EVENT_ID
import org.inu.events.objects.IntentMessage.MY_WROTE
import org.inu.events.service.LoginService
import org.inu.events.viewmodel.DetailViewModel
import org.koin.android.ext.android.inject

class DetailActivity : AppCompatActivity() {
    companion object {
        fun callingIntent(context: Context, eventId: Int = -1, myWrote: Boolean? = false) =
            Intent(context, DetailActivity::class.java).apply {
                putExtra(EVENT_ID, eventId)
                putExtra(MY_WROTE, myWrote)
            }
        private const val SHARED_PREFERENCES_NAME = "alarm"
    }

    // 전역 변수로 변경
    private var id: Int = -1

    private val loginService: LoginService by inject()
    private val viewModel: DetailViewModel by viewModels()
    private lateinit var binding: ActivityDetailBinding

    private val googleLogin = GoogleLoginWrapper(this)

    private val bottomDialog = BottomSheetDialog()
    private val alarmDialog = AlarmDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()

        initCommentButton()
        initNotificationButton()

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

    private fun initNotificationButton() {
        observe(viewModel.alarmClickEvent) {
            if (loginService.isLoggedIn) {
                if (viewModel.notificationOnOff.value == false) {
                    bottomDialog.show(
                        this,
                        { viewModel.postNotification("start")
                            alarmDialog.showDialog(this, resources.getString(R.string.alarm_on_title_start), resources.getString(R.string.alarm_on_content_start))
                        },
                        { viewModel.postNotification("end")
                            alarmDialog.showDialog(this, resources.getString(R.string.alarm_on_title_last), resources.getString(R.string.alarm_on_content_last))
                        },
                        {
                            // 취소 클릭
                        })
                } else {
                    viewModel.deleteNotification()
                    alarmDialog.showDialog(this, resources.getString(R.string.alarm_off_title), resources.getString(R.string.alarm_off_content)
                    )
                }
            }
            else{
                toast("로그인을 하셔야 알람을 받으실 수 있습니다!")
            }
        }
    }

    private fun setupToolbar() {
        binding.detailToolbar.toolbarImageView.setOnClickListener { finish() }
        //todo - 툴바메뉴는 자신이 작성한 글일 경우에만 노출돼야함
        if (loginService.isLoggedIn) {
            if (isMyWriting()) {
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
                startActivity(RegisterEventsActivity.callingIntent(this, viewModel.eventIndex))
                true
            }
            R.id.deleteToolbarMenu -> {
                viewModel.onDeleteClickEvent()
                finish()
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
    }

    override fun onResume() {
        super.onResume()
        setupToolbar()
    }

    private fun extractEventIdAndLoad() {
        id = getIntExtra(EVENT_ID) ?: return
        viewModel.load(id)
    }

    //private fun isMyWriting() = getBooleanExtra(MY_WROTE) ?: false

    //개발할 땐 불편하니까 일단 true 로 설정할게요~ 위에있는 코드가 진짜입니당!
    private fun isMyWriting() = true
}