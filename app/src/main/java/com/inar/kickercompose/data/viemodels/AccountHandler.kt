package com.inar.kickercompose.data.viemodels

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.inar.kickercompose.data.models.states.auth.AuthState
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.auth0.android.jwt.JWT
import com.inar.kickercompose.data.models.account.*
import com.inar.kickercompose.data.models.states.MessageState
import com.inar.kickercompose.data.models.states.MessageStyle
import com.inar.kickercompose.data.net.repositories.interfaces.IAccountRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountHandler @Inject constructor(
    private val account: IAccountRepository,
    @ApplicationContext private val appContext: Context,
) : ViewModel() {

    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore("account")
        val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        const val tag = "account_vm"
    }

    suspend fun getRefreshToken(): String? =
        appContext.dataStore.data.first()[REFRESH_TOKEN_KEY]

    suspend fun setRefreshToken(value: String) {
        appContext.dataStore.edit {
            it[REFRESH_TOKEN_KEY] = value
        }
    }

    suspend fun getAccessToken(): String? =
        appContext.dataStore.data.first()[ACCESS_TOKEN_KEY]

    suspend fun setAccessToken(value: String) {
        appContext.dataStore.edit {
            it[ACCESS_TOKEN_KEY] = value
        }
    }

    private suspend fun setTokens(refreshToken: String, accessToken: String) {
        setRefreshToken(refreshToken)
        setAccessToken(accessToken)
    }


    val authState: LiveData<AuthState>
        get() = _authState

    private val _authState = MutableLiveData<AuthState>(AuthState.Loading(""))

    suspend fun loginRefresh() {
        val token = getRefreshToken()
        if (token == "" || token == null) {
            _authState.value = AuthState.NotLogged("")
            return
        }
        _authState.value = AuthState.Loading("")

        try {
            val loginDto =
                account.refresh(RefreshDto().apply { refreshToken = token })

            if (loginDto.success) {
                setTokens(refreshToken = loginDto.refreshToken,
                    accessToken = loginDto.accessToken)
                _authState.value = AuthState.Logged("")
            } else {
                setTokens("", "")
                _authState.value = AuthState.NotLogged("");
            }

        } catch (e: CancellationException) {
            Log.e(tag, e.message ?: "job was cancelled")
        } catch (e: HttpException) {
          val message =   (e.localizedMessage ?: "http error") + "\n" + (e.response()?.errorBody()?.string()
                ?: "no error body")
            Log.e(tag, message)
            _authState.value = AuthState.Error("", e)
        } catch (e: Exception) {
            Log.e(tag, e.message ?: "")
            _authState.value = AuthState.Error("", e)
        }

    }

    suspend fun login(loginDto: LoginDto): MessageState {
        try {
            val answerDto = account.loginMail(loginDto)

            return if (answerDto.success) {
                setTokens(refreshToken = answerDto.refreshToken,
                    accessToken = answerDto.accessToken)
                _authState.value = AuthState.Logged("")
                MessageState(answerDto.message, MessageStyle.Success)
            } else {
                setTokens("", "")
                MessageState(answerDto.message, MessageStyle.NotCorrect)
            }

        } catch (e: Exception) {
            return MessageState(e.localizedMessage ?: "error", MessageStyle.Error)
        }
    }

    suspend fun logOut() {
        account.logOut(getAccessToken() ?: "i ", LogoutDto(getRefreshToken() ?: ""))
        setTokens("", "");
        _authState.value = AuthState.NotLogged("")
    }

    suspend fun register(registerDto: RegisterDto): MessageState {
        try {
            val dto = account.mailRegistration(registerDto)
            return MessageState(dto.message,
                if (dto.success) MessageStyle.Success else MessageStyle.NotCorrect)
        } catch (e: Exception) {
            Log.e(tag, e.localizedMessage ?: "")
            return MessageState(e.localizedMessage ?: "error", MessageStyle.Error)
        }
    }

    suspend fun getUserClaims(): MeByClaims? {
        val token = getAccessToken()
        if (token == null || token == "")
            return null
        val jwt = JWT(token);
        val id =
            jwt.getClaim("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/sid").asString()!!
        val name =
            jwt.getClaim("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name").asString()!!
        return MeByClaims().also {
            it.id = id
            it.name = name
        }
    }
}