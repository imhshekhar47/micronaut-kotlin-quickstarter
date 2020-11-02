package org.hshekhar.controller

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.swagger.v3.oas.annotations.tags.Tag
import java.security.Principal
import javax.annotation.Nullable

data class Info (val name: String, val version: String)

@Tag(name = "Info")
@Controller("/api")
@Secured(SecurityRule.IS_ANONYMOUS)
class ApiController {

    @Get
    fun info() : Info {
        return Info(name = "Micronaut-Apis", version = "1.0.1")
    }

    @Get("whoami")
    fun whoami(@Nullable principal: Principal?): String {
        return principal?.name?: "ANONYMOUS"
    }
}