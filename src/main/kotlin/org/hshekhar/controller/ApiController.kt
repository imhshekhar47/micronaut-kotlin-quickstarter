package org.hshekhar.controller

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.swagger.v3.oas.annotations.tags.Tag

data class Info (val name: String, val version: String)

@Tag(name = "Info")
@Controller("/api")
class ApiController {

    @Get
    fun info() : Info {
        return Info(name = "Micronaut-Apis", version = "1.0.1")
    }
}