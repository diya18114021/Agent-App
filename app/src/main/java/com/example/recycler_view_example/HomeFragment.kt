package com.example.recycler_view_example

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_home.recycler_view
import kotlinx.android.synthetic.main.fragment_home.refreshLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    private lateinit var viewModel: HomeViewModel

    var adapter : HomeAdapter? = null

    override fun onStop() {
        if(adapter != null)
            adapter!!.onStop()
        super.onStop()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        // TODO: Use the ViewModel
        //Init
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
                Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<List<ExampleItem>>,
                response: Response<List<ExampleItem>>
            ) {
                refreshLayout.isRefreshing = false
                val products = response.body()
                products?.let {
                    showProducts(it)
                }
            }
        })
    }

    private fun showProducts(products: List<ExampleItem>){
        recycler_view.adapter = HomeAdapter(requireContext(),products)
        recycler_view.layoutManager = LinearLayoutManager(activity)
    }

}