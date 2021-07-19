package com.example.recycler_view_example

import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CartViewModel : ViewModel() {
    private val compositeDisposable:CompositeDisposable
    private var iCartDataSource:ICartDataSource? = null
    private var mutableLiveDataCartItem:MutableLiveData<List<Cart>>? = null

    init {
        compositeDisposable = CompositeDisposable()
    }

    fun initCartDataSource(context: Context) {
        iCartDataSource = CartDataSource(CartDatabase.getInstance(context).cartDAO())
    }

    fun getMutableLiveDataCartItem() : MutableLiveData<List<Cart>>{
        if(mutableLiveDataCartItem == null) {
            mutableLiveDataCartItem = MutableLiveData()
        }
            getAllCart()
            return mutableLiveDataCartItem!!
    }

    private fun getAllCart() {
        compositeDisposable.addAll(iCartDataSource!!.getCartItems()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ cartItems ->
                mutableLiveDataCartItem!!.value = cartItems
            }, {t: Throwable? -> mutableLiveDataCartItem!!.value = null}))
    }

    fun onStop() {
        compositeDisposable.clear()
    }

}