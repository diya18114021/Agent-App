package com.example.recycler_view_example

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ExampleAdapter(private val exampleList: List<ExampleItemWithCount>) : RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder>(){

    private var clickPosition = -1
    companion object {
        val  cartItems  = mutableSetOf<ExampleItemWithCount>()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_recyclerview_model,
        parent, false)
        return ExampleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
        val currentItem = exampleList[position]
        val exampleItem = currentItem.component1()
        holder.textView1.text = exampleItem.text1
        holder.textView2.text = exampleItem.text2
        holder.textView3.text = exampleItem.price1
        holder.textView4.text = exampleItem.price2
        holder.textView5.text = exampleItem.diff

        holder.addtocartButton.setOnClickListener {
            clickPosition = position
            currentItem.itemCount = 1
            notifyDataSetChanged()
        }

        holder.addButton.setOnClickListener {
            currentItem.itemCount+=1
            holder.countText.text = "${currentItem.itemCount}"
        }
        holder.minusButton.setOnClickListener {
            currentItem.itemCount -= 1
            if(currentItem.itemCount < 1){
                holder.addingCart.visibility = View.GONE
                holder.addtocartButton.visibility = View.VISIBLE
            }
            holder.countText.text = "${currentItem.itemCount}"
        }
        if (clickPosition == position) {
            holder.addtocartButton.visibility = View.GONE
            currentItem.itemCount = 1
            holder.countText.text = "${currentItem.itemCount}"
            holder.addingCart.visibility = View.VISIBLE
        }

        if(currentItem.itemCount > 0){
            cartItems.add(currentItem)
        }

        Glide.with(holder.itemView.context)
            .load(exampleItem.imageResource)
            .into(holder.imageView)
    }

    override fun getItemCount() = exampleList.size

    class ExampleViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.Item_Image)
        val textView1: TextView = itemView.findViewById(R.id.Item_Name)
        val textView2: TextView = itemView.findViewById(R.id.Item_Info)
        val textView3: TextView = itemView.findViewById(R.id.Item_Market_Price_View)
        val textView4: TextView = itemView.findViewById(R.id.Item_Selling_Price_View)
        val textView5: TextView =itemView.findViewById(R.id.Item_Profit_View)
        val countText: TextView = itemView.findViewById(R.id.itemCount)

        val addtocartButton : Button = itemView.findViewById(R.id.add_to_cart_button)
        val addingCart : RelativeLayout = itemView.findViewById(R.id.addingcart)
        val addButton: ImageButton = itemView.findViewById(R.id.increase_value)
        val minusButton : ImageButton = itemView.findViewById(R.id.decrease_value)
    }
}