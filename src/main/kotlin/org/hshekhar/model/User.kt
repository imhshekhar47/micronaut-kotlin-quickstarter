package org.hshekhar.model

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class User(val email: String, val password: String?, val roles: List<String> = listOf("GUEST"))