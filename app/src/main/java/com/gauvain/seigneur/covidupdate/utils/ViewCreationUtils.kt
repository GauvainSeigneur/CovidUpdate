package com.gauvain.seigneur.covidupdate.utils

import android.content.res.TypedArray

fun TypedArray.use(block: TypedArray.() -> Unit) {
    try {
        block()
    } finally {
        this.recycle()
    }
}