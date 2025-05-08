package com.android.homequest.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("_id") val id: String? = null,
    var lastname: String,
    var firstname: String,
    var role: String,
    var email: String,
    var password: String,
    var points: Int? = null

)