package org.inu.events

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.inu.events.common.extension.*
import org.inu.events.databinding.ActivityDetailBinding
import org.inu.events.dialog.AlarmDialog
import org.inu.events.dialog.BottomSheetDialog
import org.inu.events.dialog.BottomSheetDialogOneButton
import org.inu.events.dialog.LoginDialog
import org.inu.events.googlelogin.GoogleLoginWrapper
import org.inu.events.objects.IntentMessage.EVENT_ID
import org.inu.events.objects.IntentMessage.MY_WROTE
import org.inu.events.service.LoginService
import org.inu.events.viewmodel.DetailViewModel
import org.koin.android.ext.android.inject

class DetailActivity : AppCompatActivity(),LoginDialog.LoginDialog {
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
    private val notificationBottomDialog = BottomSheetDialog(this,"알림 신청", "시작 전 알림", "마감 전 알림")
    private val menuBottomSheet = BottomSheetDialog(this,"글 메뉴","수정하기","삭제하기")
    private val bottomDialogOneButton = BottomSheetDialogOneButton(this,"알림신청")
    private val alarmDialog = AlarmDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()

        initCommentButton()
        initNotificationButton()

        setupToolbar()
        showInformation()
        showMenu()
        setTextView()
    }

    private fun setTextView() {
        binding.apply{
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
                    when(it) {
                        0 -> toast("마감된 행사입니다!")
                        1 -> bottomDialogOneButton.show(      // 시작 전 알림만
                            "시작 전 알림",
                            onOne = {viewModel.postNotification("start")
                            alarmDialog.showDialog(this, resources.getString(R.string.alarm_on_title_start), resources.getString(R.string.alarm_on_content_start))},{},)
                        2 -> bottomDialogOneButton.show(      // 마감 전 알림만
                            "마감 전 알림",
                            onOne = {viewModel.postNotification("end")
                            alarmDialog.showDialog(this, resources.getString(R.string.alarm_on_title_last), resources.getString(R.string.alarm_on_content_last))},{},)
                        3 -> notificationBottomDialog.show(    // 시작 전, 마감 전 알림 모두 뜨게
                            { viewModel.postNotification("start")
                                alarmDialog.showDialog(this, resources.getString(R.string.alarm_on_title_start), resources.getString(R.string.alarm_on_content_start))
                            },
                            { viewModel.postNotification("end")
                                alarmDialog.showDialog(this, resources.getString(R.string.alarm_on_title_last), resources.getString(R.string.alarm_on_content_last))
                            },
                            { // 취소 클릭
                            })
                        else -> toast("이게 뜰리가?")
                    }
                } else {
                    viewModel.deleteNotification()
                    alarmDialog.showDialog(this, resources.getString(R.string.alarm_off_title), resources.getString(R.string.alarm_off_content)
                    )
                }
            }
            else{
                LoginDialog().show(this, { onOk() }, { onCancel() })
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
                binding.detailToolbar.likeImageView.visibility = View.GONE
                binding.detailToolbar.iImageView.visibility = View.GONE
            }else{
                binding.detailToolbar.menuImageView.visibility = View.GONE
            }
        }else{
            binding.detailToolbar.menuImageView.visibility = View.GONE
        }
    }

    private fun showInformation() {
        observe(viewModel.informationClickEvent) {
            alarmDialog.showDialog(this, resources.getString(R.string.alarm_on_title_information), resources.getString(R.string.alarm_on_content_information))
        }
    }

    private fun showMenu(){
        observe(viewModel.menuClickEvent){
            menuBottomSheet.show(
                onFirst  = {startActivity(RegisterEventsActivity.callingIntent(this, viewModel.eventIndex))},
                onSecond = {viewModel.onDeleteClickEvent()
                            finish()},
                onCancel = {}
            )
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