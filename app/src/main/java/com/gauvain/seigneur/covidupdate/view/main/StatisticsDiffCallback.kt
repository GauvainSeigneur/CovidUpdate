package com.gauvain.seigneur.covidupdate.view.main

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import com.gauvain.seigneur.presentation.model.StatisticsItemData

class StatisticsDiffCallback(
    private var newStats: List<StatisticsItemData>,
    private var oldStats: List<StatisticsItemData>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldStats.size
    }

    override fun getNewListSize(): Int {
        return newStats.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldStats[oldItemPosition].country == newStats[newItemPosition].country
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return isContentTheSame(oldItemPosition, newItemPosition)
    }

    @Nullable
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        //you can return particular field for changed item.
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }

    private fun isContentTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldCasesData = oldStats[oldItemPosition].casesData
        val newCasesData = newStats[newItemPosition].casesData

        return oldCasesData.active == newCasesData.active
            && oldCasesData.new == newCasesData.new
            && oldCasesData.total == newCasesData.total
    }
}