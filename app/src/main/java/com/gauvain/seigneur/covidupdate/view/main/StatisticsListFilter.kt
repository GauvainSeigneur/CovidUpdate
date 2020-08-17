package com.gauvain.seigneur.covidupdate.view.main

import android.widget.Filter
import com.gauvain.seigneur.presentation.model.StatisticsItemData
import java.util.*
import kotlin.collections.ArrayList

class StatisticsListFilter(
    private val adapter: StatisticsListAdapter,
    private val statisticsList: MutableList<StatisticsItemData>,
    private val filteredStatisticsList: MutableList<StatisticsItemData>
) : Filter() {

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        filteredStatisticsList.clear()
        val charString = constraint?.toString() ?: ""
        if (charString.isEmpty()) {
            filteredStatisticsList.addAll(statisticsList)
        } else {
            val filteredList = ArrayList<StatisticsItemData>()
            statisticsList
                .filter { filter(it.country, charString) }
                .forEach { filteredList.add(it) }
            filteredStatisticsList.addAll(filteredList)
        }
        return FilterResults().apply { values = filteredStatisticsList }
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
        adapter.notifyDataSetChanged()
    }

    private fun filter(country: String, charString: String): Boolean =
        country.toLowerCase(Locale.getDefault())
            .contains(charString.toLowerCase(Locale.getDefault()))
}
