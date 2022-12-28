package org.inu.events

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.inu.events.adapter.HomeAdapter
import org.inu.events.common.extension.observe
import org.inu.events.common.extension.observeNonNull
import org.inu.events.common.extension.toast
import org.inu.events.databinding.ActivityMainBinding
import org.inu.events.dialog.LoginDialog
import org.inu.events.googlelogin.GoogleLoginWrapper
import org.inu.events.service.LoginService
import org.inu.events.viewmodel.HomeViewModel
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity(), LoginDialog.LoginDialog {
    private val loginService: LoginService by inject()
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_InuEvents) // 이것도 지울 수 있을 것 같아요.
        super.onCreate(savedInstanceState)

        initBinding()
        setupButtons()
        tryAutoLogin()
        setupRecyclerView()
        setUpSwipeRefresh()
        setupRefreshEvent()
    }

    override fun onResume() {
        super.onResume()

//        viewModel.refresh()
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainViewModel = viewModel
        binding.lifecycleOwner = this

        observeNonNull(loginService.isLoggedInLiveData) {
            viewModel.refresh()
        }
    }

    private fun setupRecyclerView() {
        val theAdapter = HomeAdapter(viewModel)

        binding.homeRecyclerView.apply {
            adapter = theAdapter  //데이터를 아답터에 전달
        }

        lifecycleScope.launch {
            viewModel.homeDataList.collectLatest {
                theAdapter.submitData(it)
            }
        }
    }

    private fun setUpSwipeRefresh() {
        binding.swipeLayout.setOnRefreshListener {
            viewModel.refresh()
            binding.swipeLayout.isRefreshing = false
        }
    }

    private fun setupButtons() {
        observe(viewModel.postClickEvent) {
            if (loginService.isLoggedIn) {
                /**
                 * refactoring 진행중인 코드로 실행하려면 76~77 주석을 해제하고, 78 을 주석처리 하세요.
                 */
//                val intent = Intent(this, TempRegisterActivity::class.java)
//                startActivity(intent)
                startActivity(RegisterEventsActivity.callingIntent(this))
            } else {
                askUserForLogin()
            }
        }
        observe(viewModel.likeClickEvent){
            if(!loginService.isLoggedIn){
                showDialog()
            }
        }
    }

    private fun setupRefreshEvent() {
        viewModel.shouldRefresh.observe(this) {
            (binding.homeRecyclerView.adapter as HomeAdapter).refresh()
        }
    }

    private fun askUserForLogin() {
        LoginDialog().show(this, ::onOk, ::onCancel)
    }

    private fun showDialog() {
        LoginDialog().show(this, { onOk() }, { onCancel() })
    }

    override fun onOk() {
        val intent = Intent(this, LoginActivity::class.java)
        this.startActivity(intent)
    }

    override fun onCancel() {
        toast("로그인을 하셔야 게시글 작성이 가능합니다")
    }

    private fun tryAutoLogin() {
        loginService.tryAutoLogin()
    }
}