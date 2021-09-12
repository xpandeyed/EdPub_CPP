package com.edpub.cpp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CurrentChapter(private var currentChapter: List<Chapter>) : RecyclerView.Adapter<CurrentChapter.ViewHolder>(){
    private var mListener: OnItemClickListener? = null
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    class ViewHolder(itemView: View, listener: CurrentChapter.OnItemClickListener?): RecyclerView.ViewHolder(itemView){
        var tvCurrentChapter: TextView = itemView.findViewById(R.id.tvCurrentChapter)
        init {
            itemView.setOnClickListener {
                listener?.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.curr_chapter, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chapter = currentChapter[position]
        holder.tvCurrentChapter.text = chapter.TITLE
    }

    override fun getItemCount(): Int {
        return currentChapter.size
    }
}