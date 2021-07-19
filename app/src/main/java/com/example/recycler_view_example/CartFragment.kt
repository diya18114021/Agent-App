package com.example.recycler_view_example

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_cart.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.reflect.Type
import java.text.NumberFormat
import java.util.*


class CartFragment : Fragment() {

    companion object {
        fun newInstance() =
            CartFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    private var iCartDataSource:ICartDataSource? = null
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var recyclerViewState: Parcelable? = null

    private lateinit var viewModel: CartViewModel

    override fun onResume() {
        super.onResume()
        calculateTotalPrice()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
        iCartDataSource = CartDataSource(CartDatabase.getInstance(requireContext()).cartDAO())
        viewModel = ViewModelProvider(this).get(CartViewModel::class.java)

        //init data source
        viewModel.initCartDataSource(requireContext())
        viewModel.getMutableLiveDataCartItem().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            showCart(it)
        })
    }

    private fun showCart(cart_bag: List<Cart>) {
        cart_recycler_view.adapter = context?.let { CartAdapter(it,cart_bag) }
        cart_recycler_view.layoutManager = LinearLayoutManager(context)
        cart_recycler_view.setHasFixedSize(true)
    }

    override fun onStart() {
        super.onStart()
        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
            Toast.makeText(context, "[Event registered]", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStop() {
        viewModel.onStop()
        compositeDisposable.clear()
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
            Toast.makeText(context, "[event unregistered]", Toast.LENGTH_SHORT).show()
        }
        super.onStop()
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onUpdateItemInCart(event : UpdateItemInCart) {
        if(event.cartItem != null){
            recyclerViewState = cart_recycler_view!!.layoutManager!!.onSaveInstanceState()
            iCartDataSource!!.updateCart(event.cartItem)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<Int>{
                    override fun onSuccess(t: Int) {
                        calculateTotalPrice()
                        cart_recycler_view!!.layoutManager!!.onRestoreInstanceState(recyclerViewState)
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onError(e: Throwable) {
                        Toast.makeText(context, "[UPDATE CART]"+e.message, Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    private fun calculateTotalPrice() {
        iCartDataSource!!.sumPrice()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<Double>{
                override fun onSubscribe(d: Disposable) {
                   
                }

                override fun onSuccess(price: Double) {
                    val locale: Locale = Locale("en", "IN")
                    val fmt: NumberFormat = NumberFormat.getCurrencyInstance(locale)
                    total_prize.text = fmt.format(price)
                }

                override fun onError(e: Throwable) {
                    Toast.makeText(context, "[SUM CART]"+e.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

}
