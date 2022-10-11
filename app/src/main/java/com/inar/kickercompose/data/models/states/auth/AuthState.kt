package com.inar.kickercompose.data.models.states.auth

import java.lang.Exception

sealed class AuthState(val refreshToken: String) {
    class Loading(refreshToken: String) : AuthState(refreshToken)
    class Logged(refreshToken: String):AuthState(refreshToken)
    class NotLogged(refreshToken: String) :AuthState(refreshToken)
    class Error(refreshToken: String,val Error: Exception): AuthState(refreshToken)
}