package com.example.recycler_view_example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recycler_view_example.ExampleAdapter.Companion.cartItems
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_recyclerview_model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Toast.makeText(applicationContext, "${cartItems.size}", Toast.LENGTH_LONG).show()

        refreshLayout.setOnRefreshListener {
            fetchProducts()
        }

        fetchProducts()
    }
        private fun fetchProducts(){
            refreshLayout.isRefreshing = true
        ExampleApi().getProducts().enqueue(object: Callback<List<ExampleItem>> {
            override fun onFailure(call: Call<List<ExampleItem>>, t: Throwable) {
                refreshLayout.isRefreshing = false
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<List<ExampleItem>>,
                response: Response<List<ExampleItem>>
            ) {
                refreshLayout.isRefreshing = false
               val exampleItem = response.body()
                val products = mutableListOf<ExampleItemWithCount>()
                val iterator = exampleItem?.listIterator()
                if (iterator != null) {
                    for(item in iterator){
                        products.add(ExampleItemWithCount(item, 0))
                    }
                }

                products.let {
                    showProducts(it)
                }
            }

        })
    }

    private fun showProducts(products: List<ExampleItemWithCount>){
        val recycler_view: RecyclerView = findViewById(R.id.recycler_view)
        recycler_view.adapter = ExampleAdapter(products)
        recycler_view.layoutManager = LinearLayoutManager(this)
    }
}