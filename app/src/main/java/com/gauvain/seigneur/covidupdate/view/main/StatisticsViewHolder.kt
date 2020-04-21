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
import kotlinx.android.synthetic.main.item_statistics.view.*

class StatisticsViewHolder(
    itemView: View,
    private val itemListener: (position: Int) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        val layout = R.layout.item_statistics
        const val BASE_FLAG_URL = "https://hatscripts.github.io/circle-flags/flags/"
        const val FLAG_IMG_FORMAT = ".svg"
    }

    fun bind(itemData: StatisticsItemData) {
        setUpCountryFlag(itemData.countryCode)
        with(itemView) {
            countryTextView.text = itemData.country
            totalCasesTextView.text = itemData.casesData.total.getFormattedString(itemView.context)
            activeCasesTextView.text = itemData.casesData.active.getFormattedString(itemView.context)
        }
        setUpNewCases(itemData.casesData.new)
    }

    private fun setUpCountryFlag(countryCode: String?) {
        with(itemView) {
            countryCode?.let {
                val imageLoader = ImageLoader(context) {
                    componentRegistry {
                        add(SvgDecoder(itemView.context))
                    }
                }
                countryFlagView.load(
                    "$BASE_FLAG_URL$it$FLAG_IMG_FORMAT",
                    imageLoader
                )
            }
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