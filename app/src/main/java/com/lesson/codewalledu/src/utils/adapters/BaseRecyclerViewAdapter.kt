package com.lesson.codewalledu.src.utils.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class BaseRecyclerViewAdapter<T>(
    private var layoutId: Int,
    private var recyclable:Boolean,
    private var stuffList: List<T>,
    private val bindView: ((position: Int, data: T, View) -> Unit),
):RecyclerView.Adapter<BaseRecyclerViewAdapter<T>.BaseViewHolder>(){


    // Public method to update the data
    @SuppressLint("NotifyDataSetChanged")
    fun setItems(newItems: List<T>) {
        this.stuffList = newItems
        notifyDataSetChanged() // Notifies RecyclerView that data has changed
        // For better performance with large lists, consider using DiffUtil
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
       BaseViewHolder(LayoutInflater.from(parent.context).inflate(layoutId,parent,false))


    override fun getItemCount(): Int = stuffList.size


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            holder.bind(position,stuffList[position])
            holder.setIsRecyclable(recyclable)
    }

    inner class BaseViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView)
    {
        fun bind(position: Int,item:T) = bindView.invoke(position,item,itemView)
    }


}

//class BaseRecyclerViewAdapter<T>(
//    private val layoutId: Int,
//    private val recyclable: Boolean = false,
//    private val bindView: ((position: Int, data: T, View) -> Unit),
//)
//    : RecyclerView.Adapter<BaseRecyclerViewAdapter<T>.BaseViewHolder>() {
//
//
//    private val items = ArrayList<T>()
//    fun setItems(items: List<T>){
//        this.items.clear()
//        this.items.addAll(items)
//        notifyDataSetChanged()
//    }
//
////    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder{
////        var view = LayoutInflater.from(context).inflate(R.layout.item,parent,false)
////        return BaseViewHolder(view)
////    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
//        BaseViewHolder(
//            LayoutInflater.from(parent.context).inflate(layoutId,parent,false)
//        )
//
//    override fun getItemCount(): Int = items.size
//
//    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
//        holder.setIsRecyclable(recyclable)
//        holder.bind(position, items[position])
//    }
//
//    inner class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        fun bind(position: Int, item: T) = bindView.invoke(position, item, itemView)
//    }
//}