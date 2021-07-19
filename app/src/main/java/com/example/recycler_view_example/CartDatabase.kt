package com.example.recycler_view_example

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [Cart::class], version = 1, exportSchema = false)
abstract class CartDatabase:RoomDatabase() {
    abstract fun cartDAO():CartDAO

    companion object {
        val DATABASE_NAME = "AgentApp-Database-Room"

        private var mInstance:CartDatabase? = null

        fun getInstance(context: Context):CartDatabase {
            if(mInstance == null)
                mInstance = Room.databaseBuilder<CartDatabase>(context, CartDatabase::class.java!!, DATABASE_NAME)
                    .build()
            return mInstance!!
        }
    }
}