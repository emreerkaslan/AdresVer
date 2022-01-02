package com.erkaslan.servio.model

import androidx.lifecycle.Observer


sealed class GenericResult<T> {
    data class Loading<T>(var loading: Boolean = true) : GenericResult<T>()
    data class Success<T>(val data: T) : GenericResult<T>()
    data class Failure<T>(val exception: ServioException?) : GenericResult<T>()
}

inline fun <reified T> genericResultWrapper(
    crossinline onLoading: () -> Unit = {},
    crossinline onSuccess: (result: T) -> Unit = {},
    crossinline onFailure: (exception: ServioException) -> Unit = {},
    crossinline onNullCase: () -> Unit = {}
): Observer<GenericResult<T?>> {
    return Observer { result ->
        when (result) {
            is GenericResult.Loading -> {
                onLoading()
            }
            is GenericResult.Success -> {
                result.data?.let(onSuccess) ?: run(onNullCase)
            }
            is GenericResult.Failure -> {
                result.exception?.let(onFailure) ?: run(onNullCase)
            }
        }
    }
}