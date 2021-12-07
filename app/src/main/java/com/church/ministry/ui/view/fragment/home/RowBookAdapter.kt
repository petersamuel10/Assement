package com.church.ministry.ui.view.fragment.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.church.ministry.R
import com.church.ministry.data.model.Book
import com.church.ministry.ui.view.PDFViewerActivity
import kotlinx.android.synthetic.main.item_layout.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class RowBookAdapter @ExperimentalCoroutinesApi constructor(
    private val bookClickListener: BookClickListener,
    private val bookList: ArrayList<Book>
) : RecyclerView.Adapter<RowBookAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(book: Book) {
            itemView.bookName.text = book.bookTitleEn
            Glide.with(itemView.bookImg.context)
                .load(book.bookPhoto)
                .into(itemView.bookImg)

            if (book.isDownload)
                itemView.syncLy.visibility = View.GONE
            else
                itemView.syncLy.visibility = View.VISIBLE

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, PDFViewerActivity::class.java)
                intent.putExtra("bookName", book.bookTitleEn)
                intent.putExtra("bookId", book.bookId)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_item, parent,
                false
            )
        )

    override fun getItemCount(): Int = bookList.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(bookList[position])

        holder.itemView.icSync.setOnClickListener {

            val rotateAnimation = RotateAnimation(
                0.0f, 360.0f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f
            )
            rotateAnimation.duration = 2.toLong() * 500
            rotateAnimation.repeatCount = Animation.INFINITE
            holder.itemView.icSync.startAnimation(rotateAnimation)
            bookClickListener.bookClickListener(bookList[position].bookId)
        }

    }

    fun addData(newList: Boolean, bookList: List<Book>) {
        if (!newList)
            this.bookList.clear()
        val lastPosition = this.bookList.size
        this.bookList.addAll(bookList)
        notifyItemRangeChanged(lastPosition, bookList.size)
    }

    fun updateBookStatus(bookId: Int) {
        val bookIndex = bookList.indexOf(bookList.find { (it.bookId == bookId) })
        bookList[bookIndex].isDownload = true
        notifyItemChanged(bookIndex)
    }
}
