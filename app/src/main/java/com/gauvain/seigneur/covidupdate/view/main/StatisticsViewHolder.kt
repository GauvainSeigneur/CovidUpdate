package com.gauvain.seigneur.covidupdate.view.main

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.api.load
import coil.decode.SvgDecoder
import com.gauvain.seigneur.covidupdate.R
import com.gauvain.seigneur.covidupdate.model.NewCasesData
import com.gauvain.seigneur.covidupdate.model.StatisticsItemData
import com.gauvain.seigneur.covidupdate.utils.loadCountry
import kotlinx.android.synthetic.main.item_statistics.view.*

class StatisticsViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        val layout = R.layout.item_statistics

    }

    fun bind(itemData: StatisticsItemData, listener: StatisticsListAdapter.Listener) {
        setUpCountryFlag(itemData.countryCode)
        with(itemView) {
            statItemView.setOnClickListener {
                listener.onClick(
                    itemData.country,
                    itemData.countryCode,
                    it.itemStatBackground,
                    it.countryFlagView)
            }
            countryTextView.text = itemData.country
            totalCasesTextView.text = itemData.casesData.total.getFormattedString(itemView.context)
            activeCasesTextView.text = itemData.casesData.active.getFormattedString(itemView.context)
        }
        setUpNewCases(itemData.casesData.new)
    }

    private fun setUpCountryFlag(countryCode: String?) {
        with(itemView) {
            countryFlagView.loadCountry(countryCode)
        }
    }

    private fun setUpNewCases(newCase: NewCasesData) {
        with(itemView) {
            newCasesTextView.text = newCase.total.getFormattedString(itemView.context)
            newCasesTextView.setTextColor(
                ContextCompat.getColor(
                    itemView.context, newCase.color
                )
            )
            newCase.icon?.let {
                val icon = ContextCompat.getDrawable(itemView.context, it)
                newCasesTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    icon,
                    null,
                    null,
                    null
                )
            } ?: newCasesTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null,
                null,
                null,
                null
            )
        }
    }
}