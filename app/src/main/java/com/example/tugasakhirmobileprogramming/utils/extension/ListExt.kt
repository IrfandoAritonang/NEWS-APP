package com.example.tugasakhirmobileprogramming.utils.extension

fun <T> List<T>?.toArrayList(): ArrayList<T> {
    val list: ArrayList<T> = arrayListOf()
    this?.forEach { item ->
        list.add(item)
    }
    return list
}