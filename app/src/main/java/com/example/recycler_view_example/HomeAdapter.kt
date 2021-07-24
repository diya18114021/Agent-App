package com.example.recycler_view_example

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus

class HomeAdapter(internal var context: Context, private val exampleList: List<HomeItem>) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>(){

    private var clickPosition = -1

    private val compositeDisposable : CompositeDisposable
    private val iCartDataSource : ICartDataSource

    init {
        compositeDisposable = CompositeDisposable()
        iCartDataSource = CartDataSource(CartDatabase.getInstance(context).cartDAO())
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.home_item,
            parent, false)
        return HomeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val currentItem = exampleList[position]
        holder.textView1.text = currentItem.text1
        holder.textView2.text = currentItem.text2
        holder.textView3.text = currentItem.price1
        holder.textView4.text = currentItem.price2
        holder.textView5.text = currentItem.diff

        holder.addtocartButton.setOnClickListener {
            clickPosition = position
            notifyDataSetChanged()
        }

        if (clickPosition == position) {
            if(holder.addtocartButton.text != "Added") {
                holder.addtocartButton.text = "Added"
                val cartItem = Cart()
                cartItem.name = currentItem.text1
                cartItem.certification = currentItem.text2
                cartItem.imageResource = currentItem.imageResource
                cartItem.sellingPrize = currentItem.price2.toInt()
                cartItem.marketPrize = currentItem.price1.toInt()
                cartItem.profit = currentItem.diff.toInt()
                cartItem.quantity = 1

                //Add to DB
                compositeDisposable.add(iCartDataSource.insertOrReplaceAll(cartItem)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        Toast.makeText(context, "Item Added to Cart", Toast.LENGTH_SHORT).show()
                        // display total items in cart
                        EventBus.getDefault().postSticky(CountCartEvent(true))
                    }, {
                        t: Throwable? -> Toast.makeText(context,"[INSERT CART]"+t!!.message,Toast.LENGTH_SHORT).show()
                    }))
            }
        }

        Glide.with(holder.itemView.context)
            .load(currentItem.imageResource)
            .into(holder.imageView)
    }

    fun onStop(){
        if(compositeDisposable != null)
        compositeDisposable.clear()
    }

    override fun getItemCount() = exampleList.size

    class HomeViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.Item_Image)
        val textView1: TextView = itemView.findViewById(R.id.Item_Name)
        val textView2: TextView = itemView.findViewById(R.id.Item_Info)
        val textView3: TextView = itemView.findViewById(R.id.Item_Market_Price_View)
        val textView4: TextView = itemView.findViewById(R.id.Item_Selling_Price_View)
        val textView5: TextView =itemView.findViewById(R.id.Item_Profit_View)

        val addtocartButton : Button = itemView.findViewById(R.id.add_to_cart_button)
    }
}