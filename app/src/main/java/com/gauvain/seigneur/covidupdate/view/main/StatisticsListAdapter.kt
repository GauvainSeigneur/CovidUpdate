package com.gauvain.seigneur.covidupdate.view.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gauvain.seigneur.presentation.model.StatisticsItemData
import com.gauvain.seigneur.presentation.utils.StringPresenter

class StatisticsListAdapter(
    private val listener: Listener
) : RecyclerView.Adapter<StatisticsViewHolder>(), Filterable {

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
    private val filteredStatisticsList = mutableListOf<StatisticsItemData>()

    /**
     * Filter widget in to filter the list
     */
    override fun getFilter(): Filter {
        return StatisticsListFilter(this, statisticsList, filteredStatisticsList)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticsViewHolder =
        StatisticsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                StatisticsViewHolder.layout,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: StatisticsViewHolder, position: Int) {
        holder.bind(filteredStatisticsList[position], listener)
    }

    override fun getItemCount(): Int {
        return filteredStatisticsList.size
    }

    fun updateStatList(newList: List<StatisticsItemData>) {
        val diffResult = DiffUtil.calculateDiff(
            StatisticsDiffCallback(
                newList,
                this.filteredStatisticsList
            )
        )
        //set up original list 
        statisticsList.clear()
        statisticsList.addAll(newList)
        //set filtered list too
        filteredStatisticsList.clear()
        filteredStatisticsList.addAll(statisticsList)
        //dispatch the result
        diffResult.dispatchUpdatesTo(this)
    }
}
