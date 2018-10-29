package com.example.a44323.studykotlin.Adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.a44323.studykotlin.R
import kotlinx.android.synthetic.main.item_test.view.*

class TestRecyclerAdapter : RecyclerView.Adapter<TestRecyclerAdapter.VH>{

    var strs:MutableList<String>? = null

    var mListener:onItemClickListener? = null

    var mListenerNew:((position:Int,v:View)->Unit)? = null

    constructor(list:MutableList<String>){
        this.strs = list
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {

        var v = LayoutInflater.from(parent.context).inflate(R.layout.item_test,parent,false)

        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.testName!!.text =  strs!!.get(position)
//        holder.itemView.setOnClickListener {
////            mListener!!.onItemClick(holder.itemView,position)
//            mListenerNew!!.invoke(position,holder.itemView)
//        }
    }

    override fun getItemCount(): Int {

        return  if (strs != null) strs!!.size else 0
    }

    public fun removeItem(position: Int){
        notifyDataSetChanged()
    }

     class VH : RecyclerView.ViewHolder {

        var itemLayout:LinearLayout? = null
        var testName:TextView? =null
        var deleteText:TextView? =null

        constructor(v:View) : super(v) {
            itemLayout = v.item_layout
            testName = v.test_name
            deleteText = v.item_delete
        }

    }
    interface onItemClickListener {

        fun onItemClick(v:View ,position: Int)
        
    }

    fun setItemClickListener(listener: onItemClickListener) {

        this.mListener = listener

    }
    fun setItemClickNew(listener:(position:Int,v:View) -> Unit){

        this.mListenerNew = listener

    }




}