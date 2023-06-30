package com.rakib.sales_agents_attendance.models

data class Store(
    var data: ArrayList<Data> = arrayListOf(),
    var links: Links? = Links(),
    var meta: Meta? = Meta()
)