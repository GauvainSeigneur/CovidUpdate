package com.gauvain.seigneur.covidupdate.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gauvain.seigneur.covidupdate.model.StatisticsItemData

class StatisticsListAdapter(
    private val itemListener: (position: Int) -> Unit
) : RecyclerView.Adapter<StatisticsViewHolder>() {

    private val statisticsList = mutableListOf<StatisticsItemData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticsViewHolder =
        StatisticsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                StatisticsViewHolder.layout,
                parent,
                false
            ),
            itemListener
        )

    override fun onBindViewHolder(holder: StatisticsViewHolder, position: Int) {
        holder.bind(statisticsList[position])
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
