package com.tamersarioglu.chuck.data.repository

import android.util.Log
import android.util.Patterns
import com.tamersarioglu.chuck.data.datastore.UserPreferencesDataStore
import com.tamersarioglu.chuck.data.model.LoginRequest
import com.tamersarioglu.chuck.data.model.RegisterRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val userPreferencesDataStore: UserPreferencesDataStore
) {
    val isLoggedIn: Flow<Boolean> = userPreferencesDataStore.isLoggedIn
    val userEmail: Flow<String> = userPreferencesDataStore.userEmail

    suspend fun login(loginRequest: LoginRequest): Result<Boolean> {

        return try {
            if (!isValidEmail(email = loginRequest.email)) return Result.failure(Exception("Invalid email format"))
            if (loginRequest.password.isBlank()) return Result.failure(Exception("Password cannot be empty"))

            val storedCredentials = userPreferencesDataStore.getStoredCredentials()
            Log.d("AuthRepository", "Stored credentials: $storedCredentials")
            Log.d(
                "AuthRepository",
                "Login attempt: ${loginRequest.email}, ${loginRequest.password}"
            )

            when (storedCredentials) {
                null -> {
                    Log.d("AuthRepository", "No stored credentials found")
                    Result.failure(Exception("No account found. Please register first."))
                }

                else -> {
                    val (storedEmail, storedPassword) = storedCredentials

                    Log.d(
                        "AuthRepository",
                        "Comparing: stored($storedEmail, $storedPassword) vs input(${loginRequest.email}, ${loginRequest.password})"
                    )

                    when {
                        loginRequest.email == storedEmail && loginRequest.password == storedPassword -> {
                            Log.d("AuthRepository", "Login successful")

                            userPreferencesDataStore.saveUserCredentials(
                                email = loginRequest.email,
                                password = loginRequest.password
                            )
                            Result.success(true)
                        }

                        else -> {
                            Log.d("AuthRepository", "Login failed - credentials don't match")
                            Result.failure(Exception("Invalid email or password"))
                        }
                    }
                }
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(registerRequest: RegisterRequest): Result<Boolean> {

        return try {

            when {
                !isValidEmail(registerRequest.email) -> {
                    Result.failure(Exception("Invalid email format"))
                }
                registerRequest.password.length < 6 -> {
                    Result.failure(Exception("Password must be at least 6 characters"))
                }
                registerRequest.password != registerRequest.confirmPassword -> {
                    Result.failure(Exception("Passwords do not match"))
                }
                else -> {
                    Log.d("AuthRepository", "Registering user: ${registerRequest.email}, ${registerRequest.password}")
                    userPreferencesDataStore.saveUserCredentials(
                        email = registerRequest.email,
                        password = registerRequest.password
                    )
                    Log.d("AuthRepository", "Registration successful")
                    Result.success(true)
                }
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        userPreferencesDataStore.logout()
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
