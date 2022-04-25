package com.inar.kickercompose.ui.account

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
import com.inar.kickercompose.data.models.account.LoginAnswerDto
import com.inar.kickercompose.data.models.account.LoginDto
import com.inar.kickercompose.data.models.account.RefreshDto
import com.inar.kickercompose.data.models.account.RegisterDto
import com.inar.kickercompose.data.models.states.MessageState
import com.inar.kickercompose.data.models.states.MessageStyle
import com.inar.kickercompose.data.net.NetworkService
import com.inar.kickercompose.data.net.repositories.AccountRepository
import com.inar.kickercompose.data.net.repositories.IAccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val account: IAccountRepository,
) : ViewModel() {

    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore("account")
        val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        const val tag = "account_vm"
    }

    suspend fun getRefreshToken(context: Context): String? =
        context.dataStore.data.first()[REFRESH_TOKEN_KEY]

    suspend fun setRefreshToken(context: Context, value: String) {
        context.dataStore.edit {
            it[REFRESH_TOKEN_KEY] = value
        }
    }

    suspend fun getAccessToken(context: Context): String? =
        context.dataStore.data.first()[ACCESS_TOKEN_KEY]

    suspend fun setAccessToken(context: Context, value: String) {
        context.dataStore.edit {
            it[ACCESS_TOKEN_KEY] = value
        }
    }

    private suspend fun setTokens(context: Context, refreshToken: String, accessToken: String) {
        setRefreshToken(context, refreshToken)
        setAccessToken(context, accessToken)
    }


    val authState: LiveData<AuthState>
        get() = _authState

    private val _authState = MutableLiveData<AuthState>(AuthState.Loading(""))

    suspend fun loginRefresh(context: Context) {
        val token = getRefreshToken(context)
        if (token == "" || token == null) {
            _authState.value = AuthState.NotLogged("")
            return
        }
        _authState.value = AuthState.Loading("")

        try {
            val loginDto =
                account.refresh(RefreshDto().apply { refreshToken = token })

            if (loginDto.success) {
                setTokens(context,
                    refreshToken = loginDto.refreshToken,
                    accessToken = loginDto.accessToken)
                _authState.value = AuthState.Logged("")
            } else {
                setTokens(context, "", "")
                _authState.value = AuthState.NotLogged("");
            }

        } catch (e: Exception) {
            Log.e(tag, e.message ?: "")
            _authState.value = AuthState.Error("", e)
        }
    }

    suspend fun login(context: Context, loginDto: LoginDto): MessageState {
        try {
            val answerDto = account.loginMail(loginDto)

            return if (answerDto.success) {
                setTokens(context,
                    refreshToken = answerDto.refreshToken,
                    accessToken = answerDto.accessToken)
                _authState.value = AuthState.Logged("")
                MessageState(answerDto.message, MessageStyle.Success)
            } else {
                setTokens(context, "", "")
                MessageState(answerDto.message, MessageStyle.NotCorrect)
            }

        } catch (e: Exception) {
            return MessageState(e.localizedMessage ?: "error", MessageStyle.Error)
        }
    }

    suspend fun logOut() {
        TODO()
    }

    suspend fun register(context: Context, registerDto: RegisterDto): MessageState {
        try {
            val dto = account.mailRegistration(registerDto)
            return MessageState(dto.message,
                if (dto.success) MessageStyle.Success else MessageStyle.NotCorrect)
        } catch (e: Exception) {
            Log.e(tag, e.localizedMessage ?: "")
            return MessageState(e.localizedMessage ?: "error", MessageStyle.Error)
        }
    }


}