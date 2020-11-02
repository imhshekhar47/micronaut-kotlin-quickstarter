# Enable JWT security in micronaut application
Refer the [Micronaut](https://micronaut-projects.github.io/micronaut-security/latest/guide/) for more details on this.

### 1. Add dependencies
Add required dependencies in `build.gradle.kts`
```kotlin
dependencies {
    kapt("io.micronaut.security:micronaut-security-annotations")
    implementation("io.micronaut.security:micronaut-security-jwt")  
}
```
### 2. Secure your resources
Add security annotations to the resources
```bash
@Secured(SecurityRule.IS_AUTHENTICATED) # only allow authenticated users
class UserController {

    @Secured(value = ["ADMIN","USER"]) # allow only to authenticated users with roles in [ADMIN, USER]
    fun getAll(): List<User> { ... }

    @Secured(value = ["ADMIN"]) # allow only to authenticated users with ADMIN role
    fun addNew(@Body user: User): User { ... }
}
```

Now if you start the service and calling these api, the access will be denied to unauthenticated users
```bash
> curl -i  -X GET http://localhost:8080/api/users 
# Response
HTTP/1.1 401 Unauthorized
connection: keep-alive
transfer-encoding: chunked
```

Now let's enable the authentication and token generation

### 3. Add configuration
First modify the application.yml to inform micronaut that what security configuration we need
```yaml
micronaut:
  security:
      authentication: bearer  # enable bearer token check in header
      token:
        jwt:
          signatures:
            secret:
              generator: 
                secret: your-secret-key-to-generate-access-token
```
This enabled `/login` endpoint which accepts user auth requests and generate access token.
### 2. Implement Security provider
Upon call of `/login` the user credential needs to be verified. This is done by `AuthenticationProvider`
```kotlin
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
```
Now we can invoke the `/login` endpoint and get the access token
### 4. Get token
```bash
> curl -i -X POST \
    -H "Content-Type: application/json; charset=utf-8" \
    -d '{"username":"admin@domain.com", "password":"admin"}' \
    http://localhost:8080/login 
# Response
HTTP/1.1 200 OK
Date: Mon, 2 Nov 2020 00:54:37 GMT
Content-Type: application/json
content-length: 350
connection: keep-alive

{"username":"admin@domain.com","roles":["ADMIN"],"access_token":"eyJhbGciOiJ...","token_type":"Bearer","expires_in":3600}
```
### 5. Access secured resources
```bash
curl -i -H "Authorization: Bearer ${ACESS_TOKEN}" -X GET http://localhost:8080/api/users
HTTP/1.1 200 OK
Date: Mon, 2 Nov 2020 01:16:48 GMT
Content-Type: application/json
content-length: 140
connection: keep-alive

[{"email":"admin@domain.com","roles":["ADMIN"]},{"email":"user@domain.com","roles":["USER"]},{"email":"guest@domain.com","roles":["GUEST"]}]
```
