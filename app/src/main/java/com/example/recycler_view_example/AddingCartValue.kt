package com.example.recycler_view_example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_cartadding.*

class AddingCartValue: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cartadding)

        var increment_number = 0
        //Increase Quantity
        increase_value.setOnClickListener {
            increment_number++
            original_value.text = increment_number.toString()
        }

        //Decrease Quantity
        decrease_value.setOnClickListener {
            increment_number--
            original_value.text = increment_number.toString()
        }

    }
}