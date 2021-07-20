package com.example.recycler_view_example

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.reactivex.disposables.CompositeDisposable
import org.greenrobot.eventbus.EventBus

class CartAdapter(internal var context: Context, internal var cartList: List<Cart>) : RecyclerView.Adapter<CartAdapter.CartViewHolder>(){

    internal var compositeDisposable : CompositeDisposable
    internal var icartDataSource:ICartDataSource

    init {
        compositeDisposable = CompositeDisposable()
        icartDataSource = CartDataSource(CartDatabase.getInstance(context).cartDAO())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.cart_item,
            parent, false)
        return CartViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val currentItem = cartList[position]
        holder.textView1.text = currentItem.name
        holder.textView2.text = currentItem.certification
        holder.textView3.text = currentItem.marketPrize.toString()
        holder.textView4.text = currentItem.sellingPrize.toString()
        holder.textView5.text = currentItem.profit.toString()
        holder.textView6.text = currentItem.marketPrize.toString() + " /piece"
        holder.textView7.text = currentItem.profit.toString() + " /piece"
        holder.textView8.text = "${currentItem.quantity}"
        holder.textView9.text = "${currentItem.quantity}"
        holder.textView10.text = "${currentItem.quantity * currentItem.marketPrize}"
        holder.textView11.text = "${currentItem.quantity * currentItem.profit}"
        holder.textView12.text = "${currentItem.quantity * (currentItem.marketPrize + currentItem.profit)}"
        holder.countText.text = "${currentItem.quantity}"

        holder.addButton.setOnClickListener {
            currentItem.quantity+=1
            holder.textView8.text = "${currentItem.quantity}"
            holder.textView9.text = "${currentItem.quantity}"
            holder.textView10.text = "${currentItem.quantity * currentItem.marketPrize}"
            holder.textView11.text = "${currentItem.quantity * currentItem.profit}"
            holder.textView12.text = "${currentItem.quantity * (currentItem.marketPrize + currentItem.profit) }"
            EventBus.getDefault().postSticky(UpdateItemInCart(currentItem))
        }
        holder.minusButton.setOnClickListener {
            if(currentItem.quantity != 1) {
                currentItem.quantity -= 1
                holder.textView8.text = "${currentItem.quantity}"
                holder.textView9.text = "${currentItem.quantity}"
                holder.textView10.text = "${currentItem.quantity * currentItem.marketPrize}"
                holder.textView11.text = "${currentItem.quantity * currentItem.profit}"
                holder.textView12.text = "${currentItem.quantity * (currentItem.marketPrize + currentItem.profit) }"
                EventBus.getDefault().postSticky(UpdateItemInCart(currentItem))
            }
        }

        Glide.with(holder.itemView.context)
            .load(currentItem.imageResource)
            .into(holder.imageView)
    }

    override fun getItemCount() = cartList.size

    class CartViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.cart_item_Image)
        val textView1: TextView = itemView.findViewById(R.id.cart_item_Name)
        val textView2: TextView = itemView.findViewById(R.id.cart_item_Info)
        val textView3: TextView = itemView.findViewById(R.id.Item_Market_Price_View)
        val textView4: TextView = itemView.findViewById(R.id.Item_Selling_Price_View)
        val textView5: TextView =itemView.findViewById(R.id.Item_Profit_View)
        val countText: TextView = itemView.findViewById(R.id.cartitemCount)

        val textView6: TextView = itemView.findViewById(R.id.fillingTableMarketPrice)
        val textView7: TextView = itemView.findViewById(R.id.fillingTableCommission)
        val textView8: TextView = itemView.findViewById(R.id.fillingTableItemQuantity1)
        val textView9: TextView = itemView.findViewById(R.id.fillingTableItemQuantity2)
        val textView10: TextView = itemView.findViewById(R.id.fillingTotal1)
        val textView11: TextView = itemView.findViewById(R.id.fillingTotal2)
        val textView12: TextView = itemView.findViewById(R.id.fillingOverallTotal)

        val addButton: ImageButton = itemView.findViewById(R.id.increase_value_in_cart)
        val minusButton : ImageButton = itemView.findViewById(R.id.decrease_value_in_cart)
    }
}