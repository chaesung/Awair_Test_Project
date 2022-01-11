package com.awair.testproject.network

/**
 * Util for retrofit coroutine flow
 * https://levelup.gitconnected.com/android-basic-app-using-mvvm-hilt-coroutines-flow-retrofit-and-coil-433763542ee0
 */
sealed class NetworkResult<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(message: String, data: T? = null) : NetworkResult<T>(data, message)
    class Loading<T> : NetworkResult<T>()
}