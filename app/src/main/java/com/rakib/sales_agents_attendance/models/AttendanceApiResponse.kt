package com.rakib.sales_agents_attendance.models

import com.google.gson.annotations.SerializedName

data class AttendanceApiResponse(
    var code: Int? = null,

    @SerializedName("app_message")
    var appMessage: String? = null,

    @SerializedName("user_message")
    var userMessage: String? = null,

    var data: AttendanceData? = AttendanceData()
)