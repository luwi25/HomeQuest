package com.android.homequest.model

import java.util.Date

data class TaskAssignment (
    var assignTo: String,
    var assignToEmail: String,
    var taskname: String,
    var taskpoints: Int,
    var assignDate: String,
    var status: String

)