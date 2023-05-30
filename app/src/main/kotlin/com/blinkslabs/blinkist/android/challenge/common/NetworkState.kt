package com.blinkslabs.blinkist.android.challenge.common

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}


class NetworkState(val status: Status, val msg: String) {

    companion object {

        val LOADED: NetworkState = NetworkState(
            Status.SUCCESS,
            STATUS_LOADED_MESSAGE
        )
        val LOADING: NetworkState = NetworkState(
            Status.RUNNING,
            STATUS_LOADING_MESSAGE
        )

        val ERROR: NetworkState = NetworkState(
            Status.FAILED,
            STATUS_ERROR_MESSAGE
        )

        val NO_RESULTS: NetworkState = NetworkState(
            Status.FAILED,
            STATUS_NO_RESULTS_MESSAGE
        )

    }

}

const val STATUS_ERROR_MESSAGE = "An unexpected error appeared, please retry later..."
const val STATUS_NO_RESULTS_MESSAGE = "No results! Please check your internet connection..."
const val STATUS_LOADING_MESSAGE = "Running"
const val STATUS_LOADED_MESSAGE = "Success"