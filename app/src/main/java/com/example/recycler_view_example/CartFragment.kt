package com.example.recycler_view_example

import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recycler_view_example.payment_gateway_helpers.CoreHelper
import com.example.recycler_view_example.payment_gateway_helpers.apihelper.RetrofitClientInstance
import com.example.recycler_view_example.payment_gateway_helpers.apihelper.RetrofitInterface
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONObject
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
    private var finalPrize : Double? = null

    private lateinit var viewModel: CartViewModel
    private val TAG = "CartFragment"

    override fun onResume() {
        super.onResume()
        calculateTotalPrice()
        countCartItem()
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
        countCartItem()
        place_order_btn.setOnClickListener{
            generateOrderId()
        }

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
        }
    }

    override fun onStop() {
        viewModel.onStop()
        compositeDisposable.clear()
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
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
                    finalPrize = price
                }

                override fun onError(e: Throwable) {
                    Toast.makeText(context, "[SUM CART]"+e.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    //Display total items in cart
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onCountCartEvent(event : CountCartEvent){
        if(event.isSuccess){
            countCartItem()
        }
    }

    private fun countCartItem() {
        iCartDataSource!!.countCartItems()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<Int>{
                override fun onSuccess(t: Int) {
                    if(t == 1){
                        total_items.text = StringBuilder(t.toString() + " item")
                    }
                    else{
                        total_items.text = StringBuilder(t.toString() + " items")
                    }
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Toast.makeText(context, "[COUNT CART]"+e.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    // payment integration starts from here
    private fun generateOrderId() {
        val amount = finalPrize
        if (amount != null) {
            GlobalScope.launch {
                try {
                    val merchantOrderId = CoreHelper.generateOrderId()
                    val service =
                        RetrofitClientInstance.retrofitInstance!!.create(RetrofitInterface::class.java)
                    val response = service.createOrder(merchantOrderId, amount)
                    if (response.isSuccessful) {
                        val body = response.body()
                        val rzpOrderId = body?.get("rzp_order_id")
                        val rzpId = body?.get("rzp_id")
                        if (rzpId != null && rzpOrderId != null) {
                            Log.i(
                                TAG,
                                "generateOrderId: Razorpay order id: $rzpOrderId || Razorpay Id: $rzpId"
                            )
                            makePayment(
                                merchantOrderId,
                                rzpOrderId.toString(),
                                rzpId.toString(),
                                amount
                            )
                        } else {
                            activity?.runOnUiThread {
                                Toast.makeText(
                                    requireContext(),
                                    "Failed to generate order id!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        activity?.runOnUiThread {
                            Toast.makeText(
                                requireContext(),
                                "Failed to generate order id!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } catch (e: Exception) {
                    Log.e(
                        TAG,
                        "generateOrderId: Failed to generate order id! Error message: ${e.message}",
                        e
                    )
                    e.printStackTrace()
                    activity?.runOnUiThread {
                        Toast.makeText(
                            requireContext(),
                            "Failed to generate order id!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } else {
            Toast.makeText(requireContext(), "Please enter the amount!", Toast.LENGTH_LONG).show()
        }
    }

    private fun makePayment(
        merchantOrderId: String,
        rzpOrderId: String,
        rzpId: String,
        amount: Double
    ) {
        try {
            val checkout = Checkout()
            checkout.setKeyID(rzpId)
            val options = JSONObject()
            options.put("name", "Medicento")
            options.put("description", "Order Id: $merchantOrderId")
            options.put("order_id", rzpOrderId)
            options.put("currency", "INR")
            options.put("amount", amount * 100)
            options.put("prefill.name", "Medicento")
            options.put("prefill.email", "xyz@gmail.com")
            options.put("prefill.contact", "9024614510")
            val notes = JSONObject()
            notes.put("merchant_order_id", merchantOrderId)
            options.put("notes", notes)
            checkout.open(requireActivity(), options)
        } catch (e: Exception) {
            Log.e(TAG, "makePayment: Failed to make payment. Error: ${e.message}", e)
            e.printStackTrace()
            Toast.makeText(requireContext(), "Failed to make payment!", Toast.LENGTH_SHORT).show()
        }
    }

}
