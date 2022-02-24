package org.inu.events.data.repository.impl

import org.inu.events.common.db.SharedPreferenceWrapper
import org.inu.events.common.extension.orThrow
import org.inu.events.data.httpservice.AccountHttpService
import org.inu.events.data.model.dto.OAuthLoginParams
import org.inu.events.data.model.dto.LoginResult
import org.inu.events.data.model.dto.RememberedLoginParams
import org.inu.events.data.model.entity.Account
import org.inu.events.data.repository.AccountRepository

class AccountRepositoryImpl(
    private val httpService: AccountHttpService,
    private val db: SharedPreferenceWrapper
) : AccountRepository {

    /**
     * 액세스 토큰 가지고 서버에 로그인 요청 보내서 응답 받아오기.
     */
    override fun login(accessToken: String): LoginResult {
        return httpService.login(OAuthLoginParams(accessToken = accessToken)).orThrow()!!
    }

    /**
     * (자동로그인) 이메일과 자동로그인 토큰 가지고 서버에 로그인 요청 보내서 응답 받아오기.
     */
    override fun login(savedAccount: Account): LoginResult {
        return httpService.login(
            RememberedLoginParams(
                id = savedAccount.id,
                token = savedAccount.rememberMeToken
            )
        ).orThrow()!!
    }

    /**
     * 로컬에 저장된 사용자 정보(Account) 가져오기.
     */
    override fun getSavedAccount(): Account? {
        return Account(
            db.getInt("id", -1).takeIf { it != -1 } ?: return null,
            db.getString("rememberMeToken") ?: return null
        )
    }

    /**
     * 로컬에 사용자 정보(Account) 저장하기.
     */
    override fun saveAccount(account: Account) {
        db.putInt("id", account.id)
        db.putString("rememberMeToken", account.rememberMeToken)
    }
}