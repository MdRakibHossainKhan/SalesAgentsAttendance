package com.rakib.sales_agents_attendance.models

import com.google.gson.annotations.SerializedName

data class AttendanceData(
    var name: String? = null,
    var uid: String? = null,
    var latitude: String? = "0",
    var longitude: String? = "0",

    @SerializedName("request_id")
    var requestId: String? = null,

    @SerializedName("updated_at")
    var updatedAt: String? = null,

    @SerializedName("created_at")
    var createdAt: String? = null,

    var id: Int? = null
)