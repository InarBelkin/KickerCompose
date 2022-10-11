package com.inar.kickercompose.other

import com.auth0.android.jwt.JWT
import java.util.*


object StrangeUtils {

    fun decodeJwt(token: String): String {
        val jwt = JWT(token);
        val ss = jwt.getClaim("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/sid").asString()
        return ss ?: " ti loh again"
    }
//    private fun getJson(strEncoded: String): String {
//        val d = Base64.getDecoder().decode(strEncoded)
//        return String(d)
//    }


}