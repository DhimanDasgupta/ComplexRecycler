package com.dhiman.complexrecycler.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.dhiman.complexrecycler.R
import com.dhiman.complexrecycler.model.*

private const val TYPE_NONE = 0
private const val TYPE_ONE = TYPE_NONE + 1
private const val TYPE_TWO = TYPE_ONE + 1

class HorizontalAdapter(private val items: List<BaseChild>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            TYPE_ONE -> HorizontalViewHolderTypeOne(layoutInflater.inflate(R.layout.adapter_horizontal_item_type_one, parent, false))
            TYPE_TWO -> HorizontalViewHolderTypeTwo(layoutInflater.inflate(R.layout.adapter_horizontal_item_type_two, parent, false))
            else -> super.createViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        if (holder is HorizontalViewHolderTypeOne) {
            holder.bind(item = item as ChildOne)
        } else if (holder is HorizontalViewHolderTypeTwo) {
            holder.bind(item = item as ChildTwo)
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is ChildOne -> TYPE_ONE
            is ChildTwo -> TYPE_TWO
        }
    }
}

class HorizontalViewHolderTypeOne(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val card: CardView = itemView.findViewById(R.id.adapter_horizontal_type_one_card)
    private val bodyText: AppCompatTextView = itemView.findViewById(R.id.adapter_horizontal_type_one_text)

    init {
        val displayMetrics = itemView.resources.displayMetrics
        val width = (displayMetrics.widthPixels * 0.65).toInt()

        card.updateLayoutParams {
            this.width  = width
            this.height = width
        }
    }

    fun bind(item: ChildOne) {
        bodyText.text = item.toString()
    }
}

class HorizontalViewHolderTypeTwo(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val card: CardView = itemView.findViewById(R.id.adapter_horizontal_type_two_card)
    private val bodyText: AppCompatTextView = itemView.findViewById(R.id.adapter_horizontal_type_two_text)

    init {
        val displayMetrics = itemView.resources.displayMetrics
        val width = (displayMetrics.widthPixels * 0.5).toInt()

        card.updateLayoutParams {
            this.width  = width
            this.height = width
        }
    }

    fun bind(item: ChildTwo) {
        bodyText.text = item.toString()
    }
}