package com.gauvain.seigneur.covidupdate.view.main

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.api.load
import coil.decode.SvgDecoder
import com.gauvain.seigneur.covidupdate.R
import com.gauvain.seigneur.covidupdate.model.StatisticsItemData
import kotlinx.android.synthetic.main.item_statistics.view.*

class StatisticsViewHolder(
    itemView: View,
    private val itemListener: (position: Int) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        val layout = R.layout.item_statistics
        const val BASE_FLAG_URL = "https://hatscripts.github.io/circle-flags/flags"
    }

    fun bind(itemData: StatisticsItemData) {
        with(itemView) {
            itemData.countryCode?.let {
                val imageLoader = ImageLoader(context) {
                    componentRegistry {
                        add(SvgDecoder(itemView.context))
                    }
                }
                countryFlagView.load(
                    "$BASE_FLAG_URL/${it}.svg",
                    imageLoader
                )
            }
            countryTextView.text = itemData.country
            totalCasesTextView.text = itemData.casesData.total.getFormattedString(itemView.context)
            newCasesTextView.text = itemData.casesData.new.total.getFormattedString(itemView.context)
            newCasesTextView.setTextColor(
                ContextCompat.getColor(
                    itemView.context, itemData
                        .casesData.new.color
                )
            )
            itemData.casesData.new.icon?.let {
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

            activeCasesTextView.text = itemData.casesData.active.getFormattedString(itemView.context)
        }
    }

}