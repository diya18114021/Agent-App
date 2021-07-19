package com.example.recycler_view_example

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class CartRepository(private val iCartDataSource: ICartDataSource):ICartDataSource {
    override fun getCartItems(): Flowable<List<Cart>> {
        return iCartDataSource.getCartItems()
    }

    override fun getCartItemById(cartItemId: Int): Flowable<Cart> {
        return iCartDataSource.getCartItemById(cartItemId)
    }

    override fun countCartItems(): Single<Int> {
        return iCartDataSource.countCartItems()
    }

    override fun emptyCart() : Single<Int>  {
        return iCartDataSource.emptyCart()
    }

    override fun insertOrReplaceAll(vararg carts: Cart):Completable{
        return iCartDataSource.insertOrReplaceAll(*carts)
    }

    override fun updateCart(cart: Cart) : Single<Int> {
        return iCartDataSource.updateCart(cart)
    }

    override fun deleteCartItem(cart: Cart) : Single<Int> {
        return iCartDataSource.deleteCartItem(cart)
    }

    override fun sumPrice(): Single<Double> {
        return iCartDataSource.sumPrice()
    }

    companion object {
        private var mInstance : CartRepository? = null
        fun getInstance(iCartDataSource: ICartDataSource) : CartRepository{
            if(mInstance == null){
                mInstance = CartRepository(iCartDataSource)
            }
            return mInstance!!
        }
    }
}