package com.rakib.sales_agents_attendance.models

import com.google.gson.annotations.SerializedName

data class Meta(
    @SerializedName("current_page")
    var currentPage: Int? = null,

    @SerializedName("from")
    var from: Int? = null,

    @SerializedName("last_page")
    var lastPage: Int? = null,

    @SerializedName("path")
    var path: String? = null,

    @SerializedName("per_page")
    var perPage: Int? = null,

    var to: Int? = null,
    var total: Int? = null
)