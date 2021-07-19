package com.example.recycler_view_example

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface CartDAO {
    @Query("SELECT * FROM Cart")
    fun getCartItems() : Flowable<List<Cart>>

    @Query("SELECT * FROM Cart WHERE id =:cartItemId")
    fun getCartItemById(cartItemId : Int) : Flowable<Cart>

    @Query("SELECT COUNT(*) FROM Cart")
    fun countCartItems() : Single<Int>

    @Query("SELECT SUM(quantity * sellingPrize) FROM Cart")
    fun sumPrice() : Single<Double>

    @Query("DELETE FROM Cart")
    fun emptyCart() : Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplaceAll(vararg carts:Cart):Completable

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateCart(cart:Cart): Single<Int>

    @Delete
    fun deleteCartItem(cart : Cart) :Single<Int>
}