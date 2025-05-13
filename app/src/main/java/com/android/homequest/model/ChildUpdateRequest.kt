package com.android.homequest.model

data class ChildUpdateRequest(
    val childEmail: String,
    val newChildFirstname: String,
    val newChildLastname: String
)