package com.example.smspezesha.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.smspezesha.R

class SmsAdapter (private var smsList:MutableList<SmsBox>): Adapter<SmsAdapter.SmsViewHolder>(){

    inner class SmsViewHolder(itemView: View):ViewHolder(itemView){

        val msgBody:TextView = itemView.findViewById(R.id.msgBody)
        val dateBody :TextView = itemView.findViewById(R.id.textView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmsViewHolder {
        return SmsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.newsmslistitem, parent, false))
    }

    override fun onBindViewHolder(holder: SmsViewHolder, position: Int) {
        holder.dateBody.text = smsList[position].date
        holder.msgBody.text = smsList[position].msg
    }

    override fun getItemCount(): Int {
        return smsList.size
    }

    fun setSmsList(smsL:MutableList<SmsBox>){

        smsList = smsL

        notifyDataSetChanged()
    }
}