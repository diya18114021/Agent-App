package com.example.recycler_view_example

import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.item_recyclerview_model.*

interface ItemClickListener {
    fun minus(data: ExampleItemWithCount, position: Int)
    fun add(data: ExampleItemWithCount, position: Int)
    fun onAddClick(data: ExampleItemWithCount, position: Int)
}