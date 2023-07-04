package com.rakib.sales_agents_attendance

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.rakib.sales_agents_attendance.interfaces.AttendanceManager
import com.rakib.sales_agents_attendance.models.AttendanceApiResponse
import com.rakib.sales_agents_attendance.models.AttendanceData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AttendanceActivity : AppCompatActivity() {
    private var storeId: String? = null
    private var storeName: String? = null
    private var requestId: String? = null
    private lateinit var nameTextInputEditText: TextInputEditText
    private lateinit var userIdTextInputEditText: TextInputEditText
    private lateinit var submitButton: Button
    private var attendanceData: AttendanceData = AttendanceData()
    private var attendanceManager: AttendanceManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance)

        storeId = intent.getStringExtra("STORE_ID")
        storeName = intent.getStringExtra("STORE_NAME")

        requestId = storeId + storeName
        attendanceData.requestId = requestId

        nameTextInputEditText = findViewById(R.id.nameTextInputEditText)
        userIdTextInputEditText = findViewById(R.id.userIdTextInputEditText)
        submitButton = findViewById(R.id.submitButton)

        attendanceManager = RetrofitHelper.getInstance().create(AttendanceManager::class.java)

        submitButton.setOnClickListener {
            val name = nameTextInputEditText.text.toString()
            val userId = userIdTextInputEditText.text.toString()

            if (name.isEmpty() || userId.isEmpty()) {
                Toast.makeText(
                    this@AttendanceActivity,
                    "Please, Provide Name & User ID",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                attendanceData.name = name
                attendanceData.uid = userId

                try {
                    submitAttendance()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun submitAttendance() {
        attendanceManager!!.submitAttendance(attendanceData).enqueue(object :
            Callback<AttendanceApiResponse> {
            override fun onResponse(
                call: Call<AttendanceApiResponse>,
                response: Response<AttendanceApiResponse>
            ) {
                val message = response.body()!!.userMessage

                Toast.makeText(
                    this@AttendanceActivity,
                    message,
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onFailure(call: Call<AttendanceApiResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}