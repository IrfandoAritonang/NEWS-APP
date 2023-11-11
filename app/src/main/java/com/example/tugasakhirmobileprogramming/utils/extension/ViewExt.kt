package com.example.tugasakhirmobileprogramming.utils.extension

import android.view.View

fun View.gone(
) {
    visibility = View.GONE
}

fun View.visible(
) {
    visibility = View.VISIBLE
}

fun View.onClick(clickable: (v: View) -> Unit) = this.setOnClickListener { clickable(this) }
