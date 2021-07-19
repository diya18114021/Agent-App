package com.example.recycler_view_example

import com.google.gson.annotations.SerializedName

data class ExampleItem(
  val id: Int,
  @SerializedName("imageUrl")
  val imageResource: String,
  @SerializedName("name")
  val text1: String,
  @SerializedName("certification")
  val text2: String,
  @SerializedName("marketPrice")
  val price1: String,
  @SerializedName("sellingPrice")
  val price2: String,
  @SerializedName("profit")
  val diff : String
  )

data class ExampleItemWithCount(
  val item : ExampleItem,
  var itemCount : Int
)