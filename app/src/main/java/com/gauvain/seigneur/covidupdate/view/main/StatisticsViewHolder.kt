package com.gauvain.seigneur.covidupdate.view.main

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.gauvain.seigneur.covidupdate.R
import com.gauvain.seigneur.covidupdate.data.StatisticsData
import kotlinx.android.synthetic.main.item_statistics.view.*

class StatisticsViewHolder(
    itemView: View,
    private val itemListener: (position: Int) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        val layout = R.layout.item_statistics
    }

    fun bind(data: StatisticsData) {
        with(itemView) {
            countryTextView.text = data.country
            totalCasesTextView.text = data.casesData.total.toString()
        }
    }

}