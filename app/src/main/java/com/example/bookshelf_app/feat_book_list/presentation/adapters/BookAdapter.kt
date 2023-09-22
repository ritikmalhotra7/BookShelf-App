package com.example.bookshelf_app.feat_book_list.presentation.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.bookshelf_app.R
import com.example.bookshelf_app.databinding.BookItemBinding
import com.example.bookshelf_app.feat_book_list.domain.models.BookModel

class BookAdapter : RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    private var listener: ((BookModel) -> Unit)? = null
    private var favouriteClicked: ((BookModel, Boolean) -> Unit)? = null

    private val callback = object : DiffUtil.ItemCallback<BookModel>() {
        override fun areItemsTheSame(
            oldItem: BookModel,
            newItem: BookModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: BookModel,
            newItem: BookModel
        ): Boolean {
            return oldItem.toString() == newItem.toString()
        }
    }
    private val differ = AsyncListDiffer(this, callback)

    fun setList(list: List<BookModel>) {
        differ.submitList(list)
    }

    fun setClickListener(listener: (BookModel) -> Unit) {
        this.listener = listener
    }

    fun setFavouriteClicked(listener: (BookModel, Boolean) -> Unit) {
        this.favouriteClicked = listener
    }

    inner class ViewHolder(private val binding: BookItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(item: BookModel) {
            binding.apply {
                item.apply {
                    root.setOnClickListener {
                        listener?.let {
                            it(item)
                        }
                    }
                    bookItemTvTitle.text = title
                    bookItemTvHits.text = hits.toString()
                    bookItemIv.load(image)
                    bookItemIvFav.setOnClickListener {
                        isFav?.let {
                            if (it) favouriteClicked?.let {
                                it(item, true)
                            }
                            else favouriteClicked?.let {
                                it(item, false)
                            }
                        } ?: favouriteClicked?.let {
                            it(item, false)
                        }
                        if (bookItemIvFav.imageTintList == ColorStateList.valueOf(
                                root.context.getColor(
                                    R.color.color01939A
                                )
                            )
                        ) bookItemIvFav.imageTintList = ColorStateList.valueOf(Color.WHITE)
                        else bookItemIvFav.imageTintList =
                            ColorStateList.valueOf(root.context.getColor(R.color.color01939A))
                    }
                    isFav?.let {
                        if (it) bookItemIvFav.imageTintList =
                            ColorStateList.valueOf(root.context.getColor(R.color.color01939A))
                        else bookItemIvFav.imageTintList = ColorStateList.valueOf(Color.WHITE)
                    }
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            BookItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.setData(item)
    }
}