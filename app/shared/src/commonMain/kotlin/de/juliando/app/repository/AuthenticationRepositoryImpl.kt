package de.juliando.app.repository

import de.juliando.app.data.ServerDataSourceImpl
import de.juliando.app.models.objects.*

/**
 * This repository handles the authentication data.
 */

class AuthenticationRepositoryImpl(
    private val server: ServerDataSourceImpl = ServerDataSourceImpl(),
) : AuthenticationRepository {

    override suspend fun getAccount(): Auth.User {
        return server.get("/account")
    }

    override suspend fun changePassword(request: PasswordChangeRequest) {
        server.post<PasswordChangeRequest, String>("/account/password", request)
    }

    override suspend fun getUsers(): List<Auth.User> {
        return server.getList("/users")
    }

    override suspend fun getUser(username: String): List<Auth.User> {
        return server.get("/user", username)
    }

    override suspend fun addUser(request: UserAddRequest) {
        server.post<UserAddRequest, String>("/content/meal", request)
    }

    override suspend fun deleteUser(request: UserDeleteRequest) {
        server.delete<UserDeleteRequest, String>("/content/meal", request)
    }

    override suspend fun changeUserName(request: NameChangeRequest) {
        server.post<NameChangeRequest, String>("/user/name", request)
    }

    override suspend fun setUserPassword(request: PasswordChangeRequest) {
        server.post<PasswordChangeRequest, String>("/user/password", request)
    }

    override suspend fun changeUserPermission(request: PermissionChangeRequest) {
        server.post<PermissionChangeRequest, String>("/user/permission", request)
    }
}