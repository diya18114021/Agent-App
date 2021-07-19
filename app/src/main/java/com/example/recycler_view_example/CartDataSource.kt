package com.example.recycler_view_example

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class CartDataSource(private val cartDAO : CartDAO) : ICartDataSource {
    override fun getCartItems(): Flowable<List<Cart>> {
        return cartDAO.getCartItems()
    }

    override fun getCartItemById(cartItemId: Int): Flowable<Cart> {
        return cartDAO.getCartItemById(cartItemId)
    }

    override fun countCartItems(): Single<Int> {
        return cartDAO.countCartItems()
    }

    override fun emptyCart() : Single<Int> {
        return cartDAO.emptyCart()
    }

    override fun insertOrReplaceAll(vararg carts: Cart) : Completable {
        return cartDAO.insertOrReplaceAll(*carts)
    }

    override fun updateCart(cart: Cart) : Single<Int> {
        return cartDAO.updateCart(cart)
    }

    override fun deleteCartItem(cart: Cart) : Single<Int> {
        return cartDAO.deleteCartItem(cart)
    }

    override fun sumPrice(): Single<Double> {
        return cartDAO.sumPrice()
    }

    companion object {
        private var mInstance : CartDataSource? = null
        fun getInstance(cartDAO: CartDAO) : CartDataSource{
            if(mInstance == null){
                mInstance = CartDataSource(cartDAO)
            }
            return mInstance!!
        }
    }
}