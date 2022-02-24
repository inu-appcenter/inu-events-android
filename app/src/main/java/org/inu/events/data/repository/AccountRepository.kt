package org.inu.events.data.repository

import org.inu.events.data.model.dto.LoginResult
import org.inu.events.data.model.entity.Account

interface AccountRepository {
    fun login(accessToken: String): LoginResult
    fun login(savedAccount: Account): LoginResult

    fun getSavedAccount(): Account?
    fun saveAccount(account: Account)
}