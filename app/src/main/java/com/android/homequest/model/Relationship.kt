package com.android.homequest.model

import com.google.gson.annotations.SerializedName

data class Relationship (
    @SerializedName("_id") val id: String? = null,
    var parentFirstname: String,
    var parentEmail: String,
    var childFirstname: String,
    var childLastname: String,
    var childEmail: String
)