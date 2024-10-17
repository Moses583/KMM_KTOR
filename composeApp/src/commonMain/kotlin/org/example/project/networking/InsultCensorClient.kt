package org.example.project.networking

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException
import org.example.project.utils.NetworkError
import org.example.project.utils.Result

class InsultCensorClient (
    private val httpClient: HttpClient
){

    suspend fun censorWords(uncensoredWord: String): Result<String, NetworkError>{
        val response: HttpResponse = httpClient.get(
            urlString = "https://purgomalum.com/service/json"
        ) {
            parameter("text", uncensoredWord)
        }

        return when(response.status.value){
            in 200 .. 299 -> {
                val censoredWord = response.body<CensoredText>()
                Result.Success(censoredWord.result)
            }

            401 -> Result.Error(NetworkError.UNAUTHORIZED)
            409 -> Result.Error(NetworkError.CONFLICT)
            408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
            413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
            in 500 .. 599 -> Result.Error(NetworkError.SERVER_ERROR)
            else -> Result.Error(NetworkError.UNKNOWN)
        }

    }

}