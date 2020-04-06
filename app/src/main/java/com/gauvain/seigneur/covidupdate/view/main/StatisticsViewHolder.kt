package com.gauvain.seigneur.covidupdate.view.main

import android.view.View
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
    }

    fun bind(itemData: StatisticsItemData) {
        with(itemView) {
            itemData.countryCode?.let {
                val imageLoader = ImageLoader(context) {
                    componentRegistry {
                        add(SvgDecoder(itemView.context))

                    }
                }
                countryFlagView.load("https://hatscripts.github.io/circle-flags/flags/${it}.svg",
                    imageLoader)
            }
            countryTextView.text = itemData.country
            totalCasesTextView.text = itemData.casesData.total.toString()
        }
    }

}