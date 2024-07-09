package com.example.jettrivia.data

//This will wrap around a response allowing us to add more detail to the response
data class DataOrException<T, Boolean, E: Exception>(
    var data: T? = null,
    var loading: Boolean? = null,
    var e: E? = null
)