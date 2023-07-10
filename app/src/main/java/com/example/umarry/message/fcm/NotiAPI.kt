package com.example.umarry.message.fcm

import com.example.umarry.message.fcm.Repo.Companion.CONTENT_TYPE
import com.example.umarry.message.fcm.Repo.Companion.SERVER_KEY
import okhttp3.ResponseBody
import retrofit2.Response //okhttps.Response 안됌
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotiAPI {
    @Headers("Authorization: key=$SERVER_KEY", "Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(@Body notification: PushNotification): Response<ResponseBody>

}