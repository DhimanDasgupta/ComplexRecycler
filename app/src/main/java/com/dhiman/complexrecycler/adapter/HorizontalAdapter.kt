package com.dhiman.complexrecycler.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dhiman.complexrecycler.R
import com.dhiman.complexrecycler.adapter.listeners.OnChildListeners
import com.dhiman.complexrecycler.model.BaseChild
import com.dhiman.complexrecycler.model.ChildOne
import com.dhiman.complexrecycler.model.ChildTwo
import kotlin.math.min

private const val TYPE_NONE = 0
private const val TYPE_ONE = TYPE_NONE + 1
private const val TYPE_TWO = TYPE_ONE + 1

class HorizontalAdapter(private val onChildListeners: OnChildListeners) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            TYPE_ONE -> HorizontalViewHolderTypeOne(
                layoutInflater.inflate(
                    R.layout.adapter_horizontal_item_type_one,
                    parent,
                    false
                ), onChildListeners
            )
            TYPE_TWO -> HorizontalViewHolderTypeTwo(
                layoutInflater.inflate(
                    R.layout.adapter_horizontal_item_type_two,
                    parent,
                    false
                ), onChildListeners
            )
            else -> super.createViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = differ.currentList[position]
        if (holder is HorizontalViewHolderTypeOne) {
            holder.bind(item = item as ChildOne)
        } else if (holder is HorizontalViewHolderTypeTwo) {
            holder.bind(item = item as ChildTwo)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val item = differ.currentList[position]
            if (holder is HorizontalViewHolderTypeOne) {
                holder.bindChange(item = item as ChildOne)
            } else if (holder is HorizontalViewHolderTypeTwo) {
                holder.bindChange(item = item as ChildTwo)
            }
        }
    }

    override fun getItemCount() = differ.currentList.size

    override fun getItemViewType(position: Int): Int {
        return when (differ.currentList[position]) {
            is ChildOne -> TYPE_ONE
            is ChildTwo -> TYPE_TWO
        }
    }

    fun submitList(list: List<BaseChild>) {
        differ.submitList(list)
        notifyDataSetChanged()
    }

    private companion object {
        val diffCallback = object : DiffUtil.ItemCallback<BaseChild>() {
            override fun areItemsTheSame(oldItem: BaseChild, newItem: BaseChild): Boolean {
                return oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: BaseChild, newItem: BaseChild): Boolean {
                return if (oldItem is ChildOne && newItem is ChildOne) {
                    oldItem == newItem
                } else if (oldItem is ChildTwo && newItem is ChildTwo) {
                    oldItem == newItem
                } else {
                    true
                }
            }

            override fun getChangePayload(oldItem: BaseChild, newItem: BaseChild): Any? {
                if (oldItem.bookmarked != newItem.bookmarked) {
                    return Any()
                }
                return super.getChangePayload(oldItem, newItem)
            }
        }
    }
}

abstract class BaseHorizontalViewHolder(itemView: View, val onChildListeners: OnChildListeners) :
    RecyclerView.ViewHolder(itemView)

class HorizontalViewHolderTypeOne(itemView: View, onChildListeners: OnChildListeners) :
    BaseHorizontalViewHolder(itemView, onChildListeners) {
    private val card: CardView = itemView.findViewById(R.id.adapter_horizontal_type_one_card)
    private val bodyText: AppCompatTextView = itemView.findViewById(R.id.adapter_horizontal_type_one_text)
    private val bookmarkedImage: AppCompatImageView =
        itemView.findViewById(R.id.adapter_horizontal_type_one_bookmark_image)

    init {
        val displayMetrics = itemView.resources.displayMetrics
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels

        val desiredDimension = (min(width, height) * 0.65).toInt()

        card.updateLayoutParams {
            this.width = desiredDimension
            this.height = desiredDimension
        }
    }

    fun bind(item: ChildOne) {
        bodyText.text = item.toString()

        bodyText.setOnClickListener {
            onChildListeners.onChildClicked(item)
        }

        if (item.bookmarked) {
            bookmarkedImage.setImageResource(R.drawable.ic_bookmarked)
        } else {
            bookmarkedImage.setImageResource(R.drawable.ic_non_bookmarked)
        }
        bookmarkedImage.setOnClickListener {
            onChildListeners.onToggleBookmark(item)
        }
    }

    fun bindChange(item: ChildOne) {
        bodyText.text = item.toString()

        if (item.bookmarked) {
            bookmarkedImage.setImageResource(R.drawable.ic_bookmarked)
        } else {
            bookmarkedImage.setImageResource(R.drawable.ic_non_bookmarked)
        }
    }
}

class HorizontalViewHolderTypeTwo(itemView: View, onChildListeners: OnChildListeners) :
    BaseHorizontalViewHolder(itemView, onChildListeners) {
    private val card: CardView = itemView.findViewById(R.id.adapter_horizontal_type_two_card)
    private val bodyText: AppCompatTextView = itemView.findViewById(R.id.adapter_horizontal_type_two_text)

    init {
        val displayMetrics = itemView.resources.displayMetrics
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels

        val desiredDimension = (min(width, height) * 0.65).toInt()

        card.updateLayoutParams {
            this.width = desiredDimension
            this.height = desiredDimension
        }
    }

    fun bind(item: ChildTwo) {
        bodyText.text = item.toString()

        bodyText.setOnClickListener {
            onChildListeners.onChildClicked(item)
        }
    }

    fun bindChange(item: ChildTwo) {

    }
}