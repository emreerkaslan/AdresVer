package com.erkaslan.servio.model

import java.lang.RuntimeException


class ServioException : RuntimeException {
    constructor() {}
    constructor(message: String?, cause: Throwable?) : super(message, cause) {}
    constructor(message: String?) : super(message) {}
    constructor(cause: Throwable?) : super(cause) {}

    companion object {
        fun wrap(exception: Throwable): ServioException {
            return if (exception is ServioException) {
                exception
            } else {
                ServioException(exception)
            }
        }
    }
}