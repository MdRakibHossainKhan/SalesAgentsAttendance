package com.rakib.sales_agents_attendance

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.textfield.TextInputEditText
import com.rakib.sales_agents_attendance.interfaces.AttendanceManager
import com.rakib.sales_agents_attendance.models.AttendanceApiResponse
import com.rakib.sales_agents_attendance.models.AttendanceData
import com.rakib.sales_agents_attendance.utilities.RetrofitHelper
import com.rakib.sales_agents_attendance.utilities.SuccessAlertDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "LocationProvider"
private const val REQUEST_CODE_PERMISSIONS = 10

class AttendanceActivity : AppCompatActivity() {
    private var storeId: String? = null
    private var storeName: String? = null
    private var requestId: String? = null
    private lateinit var nameTextInputEditText: TextInputEditText
    private lateinit var userIdTextInputEditText: TextInputEditText
    private lateinit var submitButton: Button
    private var attendanceData: AttendanceData = AttendanceData()
    private var attendanceManager: AttendanceManager? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var location: Location? = null

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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Request Location Permission
        if (!checkPermissions()) {
            requestPermissions()
        } else {
            getLocation()
        }

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
                    getLocation()

                    submitAttendance()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions()

            return
        }

        fusedLocationClient?.lastLocation!!.addOnCompleteListener(this) { task ->
            if (task.isSuccessful && task.result != null) {
                location = task.result

                attendanceData.latitude = (location)!!.latitude.toString()
                attendanceData.longitude = (location)!!.longitude.toString()
            } else {
                Log.w(TAG, "getLocation: exception", task.exception)

                Toast.makeText(
                    this,
                    "Location not detected. Make sure location is enable on the device.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")

            Toast.makeText(
                this@AttendanceActivity,
                "Location permission is needed for core functionality.",
                Toast.LENGTH_LONG
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                this@AttendanceActivity,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            when {
                grantResults.isEmpty() -> {
                    // User Interaction was Interrupted
                    Log.i(TAG, "User interaction was cancelled.")
                }

                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    getLocation()
                }

                else -> {
                    Toast.makeText(
                        this@AttendanceActivity,
                        "Permission denied by the user.",
                        Toast.LENGTH_LONG
                    ).show()
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

                SuccessAlertDialog.showAlertDialog(
                    this@AttendanceActivity,
                    "Attendance Submitted",
                    message!!
                )
            }

            override fun onFailure(call: Call<AttendanceApiResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}