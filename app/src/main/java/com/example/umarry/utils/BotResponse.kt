package com.example.umarry.utils

import com.example.umarry.utils.Constants.OPEN_GOOGLE
import com.example.umarry.utils.Constants.OPEN_SEARCH

object BotResponse {

    fun basicResponses(_message: String): String {

        val random = (0..2).random()
        val message =_message.toLowerCase()

        return when {


            //Hello
            message.contains("안녕") -> {
                when (random) {
                    0 -> "안녕하세요!"
                    1 -> "반갑습니다!"
                    2 -> "어서옵서예!"
                    else -> "error" }
            }

            //결제
            message.contains("결제")|| message.contains("환불") -> {
                "결제 및 환불 관련 문의 사항은 010-9876-5432로 연락바랍니다."
            }

            //Matching
            message.contains("매칭") || message.contains("matching")|| message.contains("match") -> {
                "매칭관련 문의 사항은 010-1234-5678로 연락바랍니다."
            }

            //탈퇴
            message.contains("탈퇴") -> {
                "회원 탈퇴는 없습니다^^"
            }

            //Open Google
            message.contains("open") && message.contains("google")||
            message.contains("열어") && message.contains("구글")
            -> {
                OPEN_GOOGLE
            }

            //Search on the internet
            message.contains("search")||message.contains("검색")-> {
                OPEN_SEARCH
            }

            //When the programme doesn't understand...
            else -> {
                when (random) {
                    0 -> "죄송합니다. 이해하지 못했어요"
                    1 -> "다시 한번 입력해주세요"
                    else -> "error"
                }
            }
        }
    }
}