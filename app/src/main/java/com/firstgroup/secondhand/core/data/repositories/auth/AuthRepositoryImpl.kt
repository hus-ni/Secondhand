package com.firstgroup.secondhand.core.data.repositories.auth

import com.firstgroup.secondhand.core.model.Authentication
import com.firstgroup.secondhand.core.model.User
import com.firstgroup.secondhand.core.network.auth.AuthRemoteDataSource
import com.firstgroup.secondhand.core.network.auth.model.RegisterUserRequest
import com.firstgroup.secondhand.core.network.auth.model.LoginRequest
import com.firstgroup.secondhand.core.network.auth.model.UpdateUserRequest
import com.firstgroup.secondhand.core.preference.AuthPreferenceDataSource
import com.firstgroup.secondhand.core.preference.model.AuthSessionModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val authPreferenceDataSource: AuthPreferenceDataSource,
) : AuthRepository {

    override suspend fun login(
        loginRequest: LoginRequest
    ): Authentication = authRemoteDataSource.login(loginRequest).mapToDomain()

    override suspend fun register(
        registerUserRequest: RegisterUserRequest
    ): User = authRemoteDataSource.register(registerUserRequest).mapToDomain()

    override suspend fun getUser(): User = authRemoteDataSource.getUser().mapToDomain()

    override suspend fun updateUser(
        updateUserRequest: UpdateUserRequest,
    ): User = authRemoteDataSource.updateUser(
        updateUserRequest = updateUserRequest,
    ).mapToDomain()

    override fun getUserSession(): Flow<Authentication> {
        return authPreferenceDataSource.getUserSession().map {
            it.mapToDomain()
        }
    }

    override suspend fun saveUserSession(user: Authentication) {
        val authSession = AuthSessionModel(
            id = user.id,
            token = user.token,
            email = user.email,
            name = user.name
        )
        authPreferenceDataSource.saveUserSession(user = authSession)
    }

    override suspend fun deleteUserSession() {
        authPreferenceDataSource.deleteUserSession()
    }

}
