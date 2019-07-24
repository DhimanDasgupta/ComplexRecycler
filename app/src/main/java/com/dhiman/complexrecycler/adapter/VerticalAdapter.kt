package com.dhiman.complexrecycler.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.dhiman.complexrecycler.R
import com.dhiman.complexrecycler.adapter.helpers.CenterLayoutManager
import com.dhiman.complexrecycler.adapter.helpers.StartSnapHelper
import com.dhiman.complexrecycler.model.BaseParent
import com.dhiman.complexrecycler.model.ParentOne
import com.dhiman.complexrecycler.model.ParentTwo

private const val TYPE_NONE = 0
private const val TYPE_ONE = TYPE_NONE + 1
private const val TYPE_TWO = TYPE_ONE + 1

class VerticalAdapter(private val items: List<BaseParent>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            TYPE_ONE -> VerticalViewHolderTypeOne(viewPool, layoutInflater.inflate(R.layout.adapter_vertical_item_type_one, parent, false))
            TYPE_TWO -> VerticalViewHolderTypeTwo(viewPool, layoutInflater.inflate(R.layout.adapter_vertical_item_type_two, parent, false))
            else -> super.createViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        if (holder is VerticalViewHolderTypeOne) {
            holder.bind(item = item as ParentOne)
        } else if (holder is VerticalViewHolderTypeTwo) {
            holder.bind(item = item as ParentTwo)
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is ParentOne -> TYPE_ONE
            is ParentTwo -> TYPE_TWO
        }
    }
}

class VerticalViewHolderTypeOne(viewPool: RecyclerView.RecycledViewPool, itemView: View): RecyclerView.ViewHolder(itemView) {
    private val headingText: AppCompatTextView = itemView.findViewById(R.id.adapter_vertical_type_one_heading_text)
    private val recyclerView: RecyclerView = itemView.findViewById(R.id.adapter_vertical_type_one_recycler_view)
    private val layoutManager: LinearLayoutManager = CenterLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)

    init {
        recyclerView.setRecycledViewPool(viewPool)
        recyclerView.layoutManager = layoutManager
        recyclerView.isNestedScrollingEnabled = false

        val startSnapHelper = StartSnapHelper()
        startSnapHelper.attachToRecyclerView(recyclerView)
    }

    fun bind(item: ParentOne) {
        headingText.text = item.toString()
        recyclerView.adapter = HorizontalAdapter(items = item.items)
    }
}

class VerticalViewHolderTypeTwo(viewPool: RecyclerView.RecycledViewPool, itemView: View): RecyclerView.ViewHolder(itemView) {
    private val headingText: AppCompatTextView = itemView.findViewById(R.id.adapter_vertical_type_two_heading_text)
    private val recyclerView: RecyclerView = itemView.findViewById(R.id.adapter_vertical_type_two_recycler_view)
    private val layoutManager: LinearLayoutManager = CenterLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)

    init {
        recyclerView.setRecycledViewPool(viewPool)
        recyclerView.layoutManager = layoutManager
        recyclerView.isNestedScrollingEnabled = false

        val startSnapHelper = PagerSnapHelper()
        startSnapHelper.attachToRecyclerView(recyclerView)
    }

    fun bind(item: ParentTwo) {
        headingText.text = item.toString()
        recyclerView.adapter = HorizontalAdapter(items = item.items)
    }
}