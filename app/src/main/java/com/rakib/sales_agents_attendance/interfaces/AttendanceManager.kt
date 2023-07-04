package com.rakib.sales_agents_attendance.interfaces

import com.rakib.sales_agents_attendance.models.AttendanceApiResponse
import com.rakib.sales_agents_attendance.models.AttendanceData
import com.rakib.sales_agents_attendance.models.StoreApiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AttendanceManager {
    @GET("/api/stores")
    fun getStoreList(@Query("page") page: String): Call<StoreApiResponse>

    @POST("/api/attendance")
    fun submitAttendance(@Body attendanceData: AttendanceData): Call<AttendanceApiResponse>
}