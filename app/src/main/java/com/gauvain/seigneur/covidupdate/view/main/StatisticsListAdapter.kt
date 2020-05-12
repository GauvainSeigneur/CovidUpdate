package com.gauvain.seigneur.covidupdate.view.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gauvain.seigneur.covidupdate.model.StatisticsItemData
import com.gauvain.seigneur.covidupdate.utils.StringPresenter

class StatisticsListAdapter(
    private val listener: Listener
) : RecyclerView.Adapter<StatisticsViewHolder>() {

    interface Listener {
        fun onClick(
            countryName: String,
            countryCode: String?,
            rootView: View,
            flagImageView: View,
            totalCases: StringPresenter
        )
    }

    private val statisticsList = mutableListOf<StatisticsItemData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticsViewHolder =
        StatisticsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                StatisticsViewHolder.layout,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: StatisticsViewHolder, position: Int) {
        holder.bind(statisticsList[position], listener)
    }

    override fun getItemCount(): Int {
        return statisticsList.size
    }

    fun updateStatList(newList: List<StatisticsItemData>) {
        val diffResult = DiffUtil.calculateDiff(
            StatisticsDiffCallback(
                this.statisticsList,
                newList
            )
        )
        statisticsList.clear()
        statisticsList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }
}
