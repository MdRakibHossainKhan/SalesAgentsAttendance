package com.rakib.sales_agents_attendance.models

import com.google.gson.annotations.SerializedName

data class StoreApiResponse(
    @SerializedName("data")
    var storeData: ArrayList<StoreData> = arrayListOf(),

    var links: Links? = Links(),
    var meta: Meta? = Meta()
)