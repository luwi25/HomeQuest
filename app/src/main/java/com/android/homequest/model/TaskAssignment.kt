package com.android.homequest.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class TaskAssignment (
    @SerializedName("_id") val id: String? = null,
    var assignTo: String,
    var assignToEmail: String,
    var taskname: String,
    var taskpoints: Int,
    var assignDate: String,
    var status: String

)