package org.inu.events

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.inu.events.common.extension.*
import org.inu.events.databinding.ActivityDetailBinding
import org.inu.events.dialog.AlarmDialog
import org.inu.events.dialog.LoginDialog
import org.inu.events.lib.actionsheet.UniActionSheet
import org.inu.events.objects.IntentMessage.EVENT_ID
import org.inu.events.objects.IntentMessage.MY_WROTE
import org.inu.events.service.LoginService
import org.inu.events.viewmodel.DetailViewModel
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
        setTextView()
    }

    private fun setTextView() {
        binding.apply {
            textViewContact.isSelected = true
            textViewCategory.isSelected = true
            textViewTarget.isSelected = true
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
        } else {
            binding.detailToolbar.menuImageView.visibility = View.GONE
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
                        viewModel.onDeleteClickEvent()
                        finish()
                    }
                    .show()
            } else{
                UniActionSheet(this)
                    .addText("글 메뉴")
                    .addAction("신고하기") {}
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