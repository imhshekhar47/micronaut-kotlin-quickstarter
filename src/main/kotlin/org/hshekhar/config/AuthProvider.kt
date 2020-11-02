package org.hshekhar.config

import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.*
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import org.hshekhar.service.UserService
import org.reactivestreams.Publisher
import javax.inject.Singleton

@Singleton
class AuthProvider(private val userService: UserService) : AuthenticationProvider {

    override fun authenticate(
        httpRequest: HttpRequest<*>?,
        authenticationRequest: AuthenticationRequest<*, *>?
    ): Publisher<AuthenticationResponse> {
        return Flowable.create({
            val username = authenticationRequest?.identity.toString()
            val password = authenticationRequest?.secret.toString()

            val user = userService.getUserByUserNameAndPassword(username = username, password = password)
            if (null !== user) {
                val userDetail = UserDetails(user.email, user.roles)

                it.onNext(userDetail)
                it.onComplete()
            } else {
                it.onError(AuthenticationException(AuthenticationFailed()))
            }
        }, BackpressureStrategy.ERROR)


    }
}