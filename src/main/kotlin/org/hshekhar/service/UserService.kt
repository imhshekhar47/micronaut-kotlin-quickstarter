package org.hshekhar.service

import org.hshekhar.model.User
import java.util.*
import javax.inject.Singleton

@Singleton
class UserService {

    private val users = mutableListOf(
        User(email = "admin@domain.com", password = "YWRtaW4=", roles = listOf("ADMIN")),
        User(email = "user@domain.com", password = "dXNlcg==", roles = listOf("USER")),
        User(email = "guest@domain.com", password = "Z3Vlc3Q="),
    )

    private val encoder = Base64.getEncoder()

    fun getAllUsers(): List<User> {
        return users
    }

    fun getUserByUserNameAndPassword(username: String, password: String): User? {
        val base64Password = encoder.encodeToString(password.toByteArray())
        return getAllUsers().first { it.email == username && it.password == base64Password }
    }

    fun save(user: User): User {
        val base64Password = encoder.encodeToString(user.password?.toByteArray())
        val newUSer = User(email = user.email, password = base64Password, roles = listOf("USER"))

        users.add(newUSer)

        return newUSer
    }
}