package com.example.recycler_view_example

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.annotation.NonNull
import java.lang.StringBuilder

@Entity(tableName = "Cart")
class Cart {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "imageResource")
    var imageResource: String? = null

    @ColumnInfo(name = "name")
    var name: String? = null

    @ColumnInfo(name = "certification")
    var certification: String? = null

    @ColumnInfo(name = "marketPrize")
    var marketPrize: Int = 0

    @ColumnInfo(name = "sellingPrize")
    var sellingPrize: Int = 0

    @ColumnInfo(name = "profit")
    var profit: Int = 0

    @ColumnInfo(name = "quantity")
    var quantity: Int = 0

    constructor() {}

    override fun toString() : String {
        return StringBuilder(imageResource)
            .append("\n")
            .append(name)
            .append("\n")
            .append(certification)
            .toString()
    }
}
