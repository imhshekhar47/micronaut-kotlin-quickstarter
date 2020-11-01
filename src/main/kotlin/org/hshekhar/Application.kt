package org.hshekhar

import io.micronaut.runtime.Micronaut
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.info.License

@OpenAPIDefinition(
    info = Info(
        title = "Micronaut APIs",
        version = "v0.0.1",
        description = "My API",
        license = License(
            name = "Apache 2.0",
            url = "https://github.com/imhshekhar47/micronaut-kotlin-quickstarter"),
        contact = Contact(
            url = "https://github.com/imhshekhar47/micronaut-kotlin-quickstarter",
            name = "imhshekhar47",
            email = "email@domain.com")
    )
)

object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
            .packages("org.hshekhar")
            .mainClass(Application.javaClass)
            .start()
    }
}