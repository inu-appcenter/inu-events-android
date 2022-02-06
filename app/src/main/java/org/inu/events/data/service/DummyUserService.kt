package org.inu.events.data.service

import org.inu.events.data.model.Article
import org.inu.events.data.model.User

class DummyUserService: UserService {
    override fun fetchUser(): List<User> {
        return listOf(User(0,"123@gmail.com","수달","??","0","2022-01-21"),
                    User(1,"456@gmail.com","닭발","??","1","2022-01-22"),
                    User(null,"789@gmail.com","망고","??","2","2022-01-20"))
    }

    override fun getUser(userId: Int?): User {
        val userList:List<User> = fetchUser()
        if(userId == null) return userList[0]
        return userList[userId]
    }
}