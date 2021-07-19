package com.example.recycler_view_example

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface ICartDataSource {
    fun getCartItems() : Flowable<List<Cart>>
    fun getCartItemById(cartItemId : Int) : Flowable<Cart>
    fun countCartItems() : Single<Int>
    fun emptyCart() : Single<Int>
    fun insertOrReplaceAll(vararg carts:Cart): Completable
    fun updateCart(cart:Cart): Single<Int>
    fun deleteCartItem(cart : Cart) : Single<Int>
    fun sumPrice() :Single<Double>
}