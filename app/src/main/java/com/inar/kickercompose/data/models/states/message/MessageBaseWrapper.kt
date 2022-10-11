package com.inar.kickercompose.data.models.states.message

import android.content.Context
import android.util.Log
import com.inar.kickercompose.data.models.answers.MessageBase
import com.inar.kickercompose.ui.lobby.LOBBY_TAG
import com.inar.kickercompose.ui.navigation.showAlert
import retrofit2.HttpException
import java.lang.Exception
import java.lang.NullPointerException
import java.util.concurrent.CancellationException

inline fun messageBaseWrapper(inner: () -> MessageBase): MessageBase {
    try {
        return inner()
    } catch (e: HttpException) {
        return MessageBase(false,
            (e.localizedMessage ?: "http error") + "\n" + (e.response()?.errorBody()?.string()
                ?: "no error body"))
    } catch (e: NullPointerException) {
        return MessageBase(false, "programmer is a little dumb(npe)")
    } catch (e: CancellationException) {
        return MessageBase(false, "coroutine was cancelled")
    }
}

inline fun messageBaseAlertWrapper(
    context: Context,
    tag: String = "messageBaseAlert",
    inner: () -> Unit,
) {
    try {
        inner()
    } catch (e: HttpException) {
        val message =
            (e.localizedMessage ?: "http error") + "\n" + (e.response()?.errorBody()?.string()
                ?: "no error body")
        Log.e(tag, message)
        showAlert(message, context)
    } catch (e: CancellationException) {
        Log.e(tag, e.localizedMessage ?: "coroutine was cancelled")
    } catch (e: Exception) {
        Log.e(LOBBY_TAG, e.message ?: "")
        showAlert(e.localizedMessage ?: "something went wrong", context)
    }
}