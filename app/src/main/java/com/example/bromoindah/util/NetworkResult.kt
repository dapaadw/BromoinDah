package com.example.bromoindah.util

sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class Error(val message: String, val exception: Exception? = null) : NetworkResult<Nothing>()
    object Loading : NetworkResult<Nothing>()

    fun onSuccess(action: (T) -> Unit): NetworkResult<T> {
        if (this is Success) action(data)
        return this
    }

    fun onFailure(action: (String) -> Unit): NetworkResult<T> {
        if (this is Error) action(message)
        return this
    }
}
