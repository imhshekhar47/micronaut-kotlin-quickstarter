package org.hshekhar.controller

import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.swagger.v3.oas.annotations.tags.Tag
import org.hshekhar.model.User
import org.hshekhar.service.UserService


@Tag(name = "users")
@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/api/users")
class UserController(private val userService: UserService) {

    @Secured(value = ["ADMIN","USER"])
    @Get
    fun getAll(): List<User> {
        return userService.getAllUsers().map { it.copy(password = null) }
    }

    @Secured(value = ["ADMIN"])
    @Post
    fun addNew(@Body user: User): User {
        return userService.save(user)
    }
}