package org.inu.events

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import org.inu.events.adapter.CommentAdapter
import org.inu.events.common.extension.getIntExtra
import org.inu.events.common.extension.observe
import org.inu.events.common.extension.observeNonNull
import org.inu.events.common.extension.toast
import org.inu.events.common.threading.execute
import org.inu.events.databinding.ActivityCommentBinding
import org.inu.events.dialog.LoginDialog
import org.inu.events.lib.actionsheet.UniActionSheet
import org.inu.events.objects.IntentMessage.EVENT_ID
import org.inu.events.service.LoginService
import org.inu.events.viewmodel.CommentViewModel
import org.koin.android.ext.android.inject

class CommentActivity : AppCompatActivity(), LoginDialog.LoginDialog {
    companion object {
        fun callingIntent(context: Context, eventId: Int = -1) =
            Intent(context, CommentActivity::class.java).apply {
                putExtra(EVENT_ID, eventId)
            }
    }

    private val commentViewModel: CommentViewModel by viewModels()
    private val loginService: LoginService by inject()
    private lateinit var commentBinding: ActivityCommentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        setupButtons()
        setupRecyclerView()
        setupToolbar()
        this.setUpMenuWroteByMe()
        this.setUpMenuNotWroteByMe()
        setUpListener()
    }

    private fun setUpListener() {
        commentBinding.commentEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (commentBinding.commentEditText.text.length >= 300) {
                    toast("댓글은 300자 이내로 입력가능합니다.")
                }
            }
        })
    }

    private fun setupButtons() {
        observe(commentViewModel.btnClickEvent) {
            if (loginService.isLoggedIn) {
                if (!commentViewModel.content.value.isNullOrEmpty()) {
                    execute {
                        commentViewModel.postComment()
                    }.then {
                        hideEditTextKeyBoard()
                    }.catch {
                        it.printStackTrace()
                    }
                } else {
                    toast("글자를 입력하세요.")
                }
            } else {
                showDialog()
            }
        }
    }

    private fun initBinding() {
        commentBinding = DataBindingUtil.setContentView(this, R.layout.activity_comment)
        commentBinding.commentViewModel = commentViewModel
        commentBinding.lifecycleOwner = this
    }

    private fun setupRecyclerView() {
        val theAdapter = CommentAdapter(viewModel = commentViewModel)

        commentBinding.commentRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = theAdapter
        }

        observeNonNull(commentViewModel.commentList) {
            theAdapter.commentList = it
        }
    }

    private fun setupToolbar() {
        commentBinding.toolbar.setOnBackListener { finish() }
    }

    // dialog
    private fun showDialog() {      // 로그인 다이어로그 보여주기
        LoginDialog().show(this, { onOk() }, { onCancel() })
    }

    // 로그인 확인을 눌렀을 때
    override fun onOk() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    // 로그인 취소를 눌렀을 때
    override fun onCancel() {
        toast("로그인을 하셔야 댓글 작성이 가능합니다")
    }

    override fun onStart() {
        super.onStart()
        extractEventIdAndLoad()
    }

    private fun extractEventIdAndLoad() {
        val id = getIntExtra(EVENT_ID) ?: return
        commentViewModel.load(id)
    }

    private fun setUpMenuWroteByMe() {
        observe(commentViewModel.plusBtnWroteByMeClickEvent) {
            UniActionSheet(this)
                .addText("댓글 삭제")
                .addAction("삭제") { commentViewModel.deleteComment {} }
                .show()
        }
    }
    private fun setUpMenuNotWroteByMe() {
        observe(commentViewModel.plusBtnNotWroteByMeClickEvent) {
            UniActionSheet(this)
                .addText("댓글 메뉴")
                .addAction("신고하기") { } // todo - 안에 신고하기, 차단하기 기능 추가
                .addAction("사용자 차단하기") {}
                .show()
        }
    }

    private fun hideEditTextKeyBoard() {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(commentBinding.commentEditText.windowToken, 0)
    }
}