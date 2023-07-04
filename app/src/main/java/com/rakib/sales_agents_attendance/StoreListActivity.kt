package com.rakib.sales_agents_attendance

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rakib.sales_agents_attendance.adapters.StoreListAdapter
import com.rakib.sales_agents_attendance.interfaces.AttendanceManager
import com.rakib.sales_agents_attendance.models.StoreApiResponse
import com.rakib.sales_agents_attendance.models.StoreData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoreListActivity : AppCompatActivity() {
    private var currentPage = 1
    private var totalPage = 0
    private var isLastPage = false
    private var isLoading = false
    var storeListData = ArrayList<StoreData>()
    private var storeListProgressBar: ProgressBar? = null
    private var storeListAdapter: StoreListAdapter? = null
    private var attendanceManager: AttendanceManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_list)

        storeListProgressBar = findViewById(R.id.storeListProgressBar)

        val storeListRecyclerView = findViewById<RecyclerView>(R.id.storeListRecyclerView)
        // Creates a Vertical Layout Manager
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        storeListAdapter = StoreListAdapter(this)
        storeListRecyclerView.layoutManager = linearLayoutManager
        storeListRecyclerView.adapter = storeListAdapter

        attendanceManager = RetrofitHelper.getInstance().create(AttendanceManager::class.java)

        storeListRecyclerView.addOnScrollListener(object :
            PaginationScrollListener(linearLayoutManager) {
            override fun loadMoreItems() {
                this@StoreListActivity.isLoading = true
                currentPage += 1
                loadNextPage(currentPage.toString())
            }

            override val isLastPage: Boolean
                get() = false

            override val isLoading: Boolean
                get() = false
        })

        loadFirstPage(currentPage.toString())
    }

    private fun loadFirstPage(pageNum: String) {
        attendanceManager!!.getStoreList(pageNum).enqueue(object : Callback<StoreApiResponse> {
            override fun onResponse(
                call: Call<StoreApiResponse>,
                response: Response<StoreApiResponse>
            ) {
                storeListData = response.body()!!.storeData
                totalPage = response.body()!!.meta!!.lastPage!!

                storeListProgressBar!!.visibility = View.GONE

                storeListAdapter!!.addAll(storeListData)

                if (currentPage < totalPage) {
                    storeListAdapter!!.addLoadingFooter()
                } else {
                    isLastPage = true
                }
            }

            override fun onFailure(call: Call<StoreApiResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun loadNextPage(pageNum: String) {
        attendanceManager!!.getStoreList(pageNum).enqueue(object : Callback<StoreApiResponse> {
            override fun onResponse(
                call: Call<StoreApiResponse>,
                response: Response<StoreApiResponse>
            ) {
                storeListData = response.body()!!.storeData

                storeListAdapter!!.removeLoadingFooter()
                isLoading = false

                storeListAdapter!!.addAll(storeListData)

                if (currentPage < totalPage) {
                    storeListAdapter!!.addLoadingFooter()
                } else {
                    isLastPage = true
                }
            }

            override fun onFailure(call: Call<StoreApiResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}