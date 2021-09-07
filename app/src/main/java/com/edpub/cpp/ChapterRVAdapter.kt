package com.edpub.cpp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ChapterRVAdapter(private var contentList : List<Chapter>) : RecyclerView.Adapter<ChapterRVAdapter.ViewHolder>() {
    //sometime app crashes due to mListener
    private var mListener: OnItemClickListener? = null
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }
    class ViewHolder(itemView : View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView){
        var tvIndex:TextView = itemView.findViewById(R.id.tvIndex)
        var tvTitle:TextView = itemView.findViewById(R.id.tvTitle)
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item, parent, false)

        return ViewHolder(itemView, mListener!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentChapter = contentList[position]
        holder.tvIndex.text = (position+1).toString()
        holder.tvTitle.text = currentChapter.TITLE
    }

    override fun getItemCount(): Int {
        return contentList.size
    }
}