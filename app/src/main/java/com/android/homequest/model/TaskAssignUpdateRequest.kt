package com.android.homequest.model

data class TaskAssignUpdateRequest (
    val assignToEmail: String,
    val newAssignTo: String
)