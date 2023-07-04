package com.rakib.sales_agents_attendance.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.rakib.sales_agents_attendance.AttendanceActivity
import com.rakib.sales_agents_attendance.R
import com.rakib.sales_agents_attendance.models.StoreData
import java.util.LinkedList

class StoreListAdapter(private val context: Context) : RecyclerView.Adapter<ViewHolder>() {
    private var storeListData: MutableList<StoreData>? = LinkedList()
    private var isLoadingAdded = false

    // Creates New View
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var viewHolder: ViewHolder? = null
        val layoutInflater = LayoutInflater.from(parent.context)

        when (viewType) {
            ITEM -> {
                // Inflates the store_list_item View
                val viewItem = layoutInflater.inflate(R.layout.store_list_item, parent, false)
                viewHolder = StoreListViewHolder(viewItem)
            }

            LOADING -> {
                val viewLoading = layoutInflater.inflate(R.layout.progress_item, parent, false)
                viewHolder = LoadingViewHolder(viewLoading)
            }
        }

        return viewHolder!!
    }

    // Binds the List Items to a View
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsViewModel = storeListData!![position]

        when (getItemViewType(position)) {
            ITEM -> {
                val storeListViewHolder = holder as StoreListViewHolder
                storeListViewHolder.storeNameTextView.text = itemsViewModel.name
                storeListViewHolder.storeAddressTextView.text = itemsViewModel.address
            }

            LOADING -> {
                val loadingViewHolder = holder as LoadingViewHolder
                loadingViewHolder.progressBar.visibility = View.VISIBLE
            }
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, AttendanceActivity::class.java)
            intent.putExtra("STORE_ID", storeListData!![position].id.toString())
            intent.putExtra("STORE_NAME", storeListData!![position].name)
            context.startActivity(intent)
        }
    }

    // Returns the List Size
    override fun getItemCount(): Int {
        return if (storeListData == null) 0 else storeListData!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == storeListData!!.size - 1 && isLoadingAdded) LOADING else ITEM
    }

    class StoreListViewHolder(ItemView: View) : ViewHolder(ItemView) {
        val storeNameTextView: TextView = itemView.findViewById(R.id.storeNameTextView)
        val storeAddressTextView: TextView = itemView.findViewById(R.id.storeAddressTextView)
    }

    fun addLoadingFooter() {
        isLoadingAdded = true

        add(StoreData())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false

        val position = storeListData!!.size - 1
        getItem(position)

        storeListData!!.removeAt(position)
        notifyItemRemoved(position)
    }

    private fun add(storeData: StoreData) {
        storeListData!!.add(storeData)

        notifyItemInserted(storeListData!!.size - 1)
    }

    fun addAll(moveResult: List<StoreData>) {
        for (result in moveResult) {
            add(result)
        }
    }

    private fun getItem(position: Int): StoreData {
        return storeListData!![position]
    }

    inner class LoadingViewHolder(itemView: View) : ViewHolder(itemView) {
        val progressBar = itemView.findViewById<View>(R.id.progressBar) as ProgressBar
    }

    companion object {
        private const val LOADING = 0
        private const val ITEM = 1
    }
}