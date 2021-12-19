package com.church.nagwa.ui.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.recyclerview.widget.RecyclerView
import com.church.nagwa.R
import com.church.nagwa.data.model.ItemInfo
import kotlinx.android.synthetic.main.item_layout.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi


typealias Books = ArrayList<ItemInfo>

@InternalCoroutinesApi
class MainAdapter @ExperimentalCoroutinesApi constructor(
    private val bookClickListener: BookClickListener,
    private val bookList: Books
) : RecyclerView.Adapter<MainAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: ItemInfo) {
            itemView.bookName.text = item.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_layout, parent,
                false
            )
        )

    override fun getItemCount(): Int = bookList.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {

        val item = bookList[position]
        holder.itemView.bookName.text = item.name

        if (item.isDown)
            holder.itemView.syncLy.visibility = View.GONE
        else
            holder.itemView.syncLy.visibility = View.VISIBLE

        holder.itemView.icSync.setOnClickListener {

            val rotateAnimation = RotateAnimation(
                0.0f, 360.0f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f
            )
            rotateAnimation.duration = 2.toLong() * 500
            rotateAnimation.repeatCount = Animation.INFINITE
            holder.itemView.icSync.startAnimation(rotateAnimation)
            bookClickListener.bookClickListener(bookList[position], position)
        }
        holder.itemView.bookImg.setOnClickListener {
            bookClickListener.bookClickListener(bookList[position], position)
        }
    }

    fun addData(newList: Boolean, bookList: List<ItemInfo>) {
        if (!newList)
            this.bookList.clear()
        val lastPosition = this.bookList.size
        this.bookList.addAll(bookList)
        notifyItemRangeChanged(lastPosition, bookList.size)
    }

    fun updateItemStatus(id: Int) {
        val bookIndex = bookList.indexOf(bookList.find { (it.id == id) })
        bookList[bookIndex].isDown = true
        notifyItemChanged(bookIndex)
    }

}