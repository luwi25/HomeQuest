package com.android.homequest.model

import com.google.gson.annotations.SerializedName

data class Reward (
    @SerializedName("_id") val id: String? = null,
    var description: String,
    var points: Int,
    var status: String
)