package org.inu.events.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.inu.events.ui.login.LoginActivity
import org.inu.events.R
import org.inu.events.ui.register.RegisterEventsActivity
import org.inu.events.common.extension.*
import org.inu.events.databinding.ActivityDetailBinding
import org.inu.events.ui.util.dialog.AlarmDialog
import org.inu.events.ui.util.dialog.LoginDialog
import org.inu.events.lib.actionsheet.UniActionSheet
import org.inu.events.objects.IntentMessage.EVENT_ID
import org.inu.events.objects.IntentMessage.MY_WROTE
import org.inu.events.service.LoginService
import org.koin.android.ext.android.inject

class DetailActivity : AppCompatActivity(), LoginDialog.LoginDialog {
    companion object {
        fun callingIntent(context: Context, eventId: Int = -1, myWrote: Boolean? = false) =
            Intent(context, DetailActivity::class.java).apply {
                putExtra(EVENT_ID, eventId)
                putExtra(MY_WROTE, myWrote)
            }
    }

    // 전역 변수로 변경
    private var id: Int = -1

    private val loginService: LoginService by inject()
    private val viewModel: DetailViewModel by viewModels()
    private lateinit var binding: ActivityDetailBinding
    private val alarmDialog = AlarmDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()

        initCommentButton()
        initNotificationButton()

        setupToolbar()
        showMenu()
        showUserMenu()
        showLogin()
        setTextView()
    }

    private fun setTextView() {
        binding.apply {
            textViewCategory.isSelected = true
            textViewTarget.isSelected = true
            textViewContact.isSelected = true
            textViewLocation.isSelected = true
        }
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
                    when (it) {
                        0 ->
                            toast("마감된 행사입니다!")

                        1 ->
                            UniActionSheet(this)
                                .addText("알림 신청")
                                .addAction("시작 전 알림") {
                                    viewModel.postNotification("start")
                                    alarmDialog.showDialog(
                                        this,
                                        resources.getString(R.string.alarm_on_title_start),
                                        resources.getString(R.string.alarm_on_content_start)
                                    )
                                }.show()

                        2 ->
                            UniActionSheet(this)
                                .addText("알림 신청")
                                .addAction("마감 전 알림") {
                                    viewModel.postNotification("end")
                                    alarmDialog.showDialog(
                                        this,
                                        resources.getString(R.string.alarm_on_title_last),
                                        resources.getString(R.string.alarm_on_content_last)
                                    )
                                }.show()

                        3 ->
                            UniActionSheet(this)
                                .addText("알림 신청")
                                .addAction("시작 전 알림") {
                                    viewModel.postNotification("start")
                                    alarmDialog.showDialog(
                                        this,
                                        resources.getString(R.string.alarm_on_title_start),
                                        resources.getString(R.string.alarm_on_content_start)
                                    )
                                }
                                .addAction("마감 전 알림") {
                                    viewModel.postNotification("end")
                                    alarmDialog.showDialog(
                                        this,
                                        resources.getString(R.string.alarm_on_title_last),
                                        resources.getString(R.string.alarm_on_content_last)
                                    )
                                }.show()

                        else -> toast("이게 뜰리가?")
                    }
                } else {
                    viewModel.deleteNotification()
                    alarmDialog.showDialog(
                        this,
                        resources.getString(R.string.alarm_off_title),
                        resources.getString(R.string.alarm_off_content)
                    )
                }
            } else {
                LoginDialog().show(this, { onOk() }, { onCancel() })
            }
        }
    }

    private fun setupToolbar() {
        binding.detailToolbar.toolbarImageView.setOnClickListener { finish() }
        if (loginService.isLoggedIn) {
                setSupportActionBar(binding.detailToolbar.toolbarRegister)
                supportActionBar?.setDisplayShowTitleEnabled(false)
        }
    }

    private fun showLogin(){
        observe(viewModel.notLoginEvent){
            LoginDialog().show(this, { onOk() }, { toast("로그인을 하셔야 저장하실 수 있습니다!") })
        }
    }


    private fun showMenu() {
        observe(viewModel.menuClickEvent) {
            if (isMyWriting()) {
                UniActionSheet(this)
                    .addText("글 메뉴")
                    .addAction("수정하기") {
                        startActivity(
                            RegisterEventsActivity.callingIntent(
                                this,
                                viewModel.eventIndex
                            )
                        )
                    }
                    .addAction("삭제하기") {
                        LoginDialog().show(this,{
                            viewModel.onDeleteClickEvent()
                            finish()}, {},"정말 삭제하시겠습니까?")
                    }
                    .show()
            } else{
                UniActionSheet(this)
                    .addText("글 메뉴")
                    .addAction("신고하기") {
                        if (loginService.isLoggedIn){ }
                        else{
                            LoginDialog().show(this, { onOk() }, { toast("로그인을 하셔야 신고하실 수 있습니다!") })}
                    }
                    .show()
            }
        }
    }

    private fun showUserMenu(){
        observe(viewModel.userMenuClickEvent){
            UniActionSheet(this)
                .addText("사용자 메뉴")
                .addAction("차단하기"){
                    if (loginService.isLoggedIn){
                        viewModel.blockUser()
                        finish()
                    }else{
                        LoginDialog().show(this, { onOk() }, { onCancel() })
                    }
                }
                .show()
        }
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

    private fun isMyWriting() = getBooleanExtra(MY_WROTE) ?: false

    //개발할 땐 불편하니까 일단 true 로 설정할게요~ 위에있는 코드가 진짜입니당!
    //private fun isMyWriting() = true


    override fun onOk() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun onCancel() {
        toast("로그인을 하셔야 알림을 받으실 수 있습니다!")
    }
}