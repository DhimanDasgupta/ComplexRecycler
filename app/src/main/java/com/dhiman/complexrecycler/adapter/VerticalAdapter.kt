package com.dhiman.complexrecycler.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.*
import com.dhiman.complexrecycler.R
import com.dhiman.complexrecycler.adapter.helpers.CenterLayoutManager
import com.dhiman.complexrecycler.adapter.helpers.StartSnapHelper
import com.dhiman.complexrecycler.adapter.listeners.OnChildListeners
import com.dhiman.complexrecycler.adapter.listeners.OnParentListeners
import com.dhiman.complexrecycler.model.BaseParent
import com.dhiman.complexrecycler.model.ParentOne
import com.dhiman.complexrecycler.model.ParentTwo

private const val TYPE_NONE = 0
private const val TYPE_ONE = TYPE_NONE + 1
private const val TYPE_TWO = TYPE_ONE + 1

class VerticalAdapter(
    private val onParentListeners: OnParentListeners,
    private val onChildListeners: OnChildListeners
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val differ = AsyncListDiffer(this, diffCallback)
    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            TYPE_ONE -> VerticalViewHolderTypeOne(
                layoutInflater.inflate(R.layout.adapter_vertical_item_type_one, parent, false),
                viewPool,
                onParentListeners,
                onChildListeners
            )
            TYPE_TWO -> VerticalViewHolderTypeTwo(
                layoutInflater.inflate(R.layout.adapter_vertical_item_type_two, parent, false),
                viewPool,
                onParentListeners,
                onChildListeners
            )
            else -> super.createViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = differ.currentList[position]
        if (holder is VerticalViewHolderTypeOne) {
            holder.bind(item = item as ParentOne)
        } else if (holder is VerticalViewHolderTypeTwo) {
            holder.bind(item = item as ParentTwo)
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
            if (holder is VerticalViewHolderTypeOne) {
                holder.bindChange(item = item as ParentOne)
            } else if (holder is VerticalViewHolderTypeTwo) {
                holder.bindChange(item = item as ParentTwo)
            }
        }
    }

    override fun getItemCount() = differ.currentList.size

    override fun getItemViewType(position: Int): Int {
        return when (differ.currentList[position]) {
            is ParentOne -> TYPE_ONE
            is ParentTwo -> TYPE_TWO
        }
    }

    fun submitList(list: List<BaseParent>) {
        differ.submitList(list)
        notifyDataSetChanged()
    }

    private companion object {
        val diffCallback = object : DiffUtil.ItemCallback<BaseParent>() {
            override fun areItemsTheSame(oldItem: BaseParent, newItem: BaseParent): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: BaseParent, newItem: BaseParent): Boolean {
                return oldItem.id == newItem.id
            }

            override fun getChangePayload(oldItem: BaseParent, newItem: BaseParent): Any? {
                var hasChange = oldItem.items.size != newItem.items.size
                for (i in 0 until oldItem.items.size) {
                    hasChange = oldItem.items[i].bookmarked != newItem.items[i].bookmarked
                    if (hasChange) {
                        break
                    }
                }

                if (hasChange) {
                    return Any()
                }
                return super.getChangePayload(oldItem, newItem)
            }
        }
    }
}


abstract class BaseVerticalViewHolder(
    itemView: View,
    val viewPool: RecyclerView.RecycledViewPool,
    val onParentListeners: OnParentListeners,
    val onChildListeners: OnChildListeners
) : RecyclerView.ViewHolder(itemView)

class VerticalViewHolderTypeOne(
    itemView: View,
    viewPool: RecyclerView.RecycledViewPool,
    onParentListeners: OnParentListeners,
    onChildListeners: OnChildListeners
) :
    BaseVerticalViewHolder(itemView, viewPool, onParentListeners, onChildListeners) {
    private val headingText: AppCompatTextView = itemView.findViewById(R.id.adapter_vertical_type_one_heading_text)
    private val collapseImage: AppCompatImageView = itemView.findViewById(R.id.adapter_vertical_type_one_image)
    private val recyclerView: RecyclerView = itemView.findViewById(R.id.adapter_vertical_type_one_recycler_view)
    private val layoutManager: LinearLayoutManager =
        CenterLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)

    init {
        recyclerView.setRecycledViewPool(this.viewPool)
        recyclerView.layoutManager = layoutManager
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = HorizontalAdapter(onChildListeners = this.onChildListeners)

        val startSnapHelper = StartSnapHelper()
        startSnapHelper.attachToRecyclerView(recyclerView)
    }

    fun bind(item: ParentOne) {
        headingText.text = item.toString()
        (recyclerView.adapter as HorizontalAdapter).submitList(item.items)

        if (item.collapsed) {
            collapseImage.setImageResource(R.drawable.ic_expand)
            recyclerView.visibility = View.GONE
        } else {
            collapseImage.setImageResource(R.drawable.ic_collapse)
            recyclerView.visibility = View.VISIBLE
        }

        collapseImage.setOnClickListener {
            onParentListeners.onCollapseExpandClicked(item)
        }
    }

    fun bindChange(item: ParentOne) {
        headingText.text = item.toString()
        (recyclerView.adapter as HorizontalAdapter).submitList(item.items)

        if (item.collapsed) {
            collapseImage.setImageResource(R.drawable.ic_expand)
            recyclerView.visibility = View.GONE
        } else {
            collapseImage.setImageResource(R.drawable.ic_collapse)
            recyclerView.visibility = View.VISIBLE
        }

        collapseImage.setOnClickListener {
            onParentListeners.onCollapseExpandClicked(item)
        }
    }
}

class VerticalViewHolderTypeTwo(
    itemView: View,
    viewPool: RecyclerView.RecycledViewPool,
    onParentListeners: OnParentListeners,
    onChildListeners: OnChildListeners
) :
    BaseVerticalViewHolder(itemView, viewPool, onParentListeners, onChildListeners) {
    private val headingText: AppCompatTextView = itemView.findViewById(R.id.adapter_vertical_type_two_heading_text)
    private val collapseImage: AppCompatImageView = itemView.findViewById(R.id.adapter_vertical_type_two_image)
    private val recyclerView: RecyclerView = itemView.findViewById(R.id.adapter_vertical_type_two_recycler_view)
    private val layoutManager: LinearLayoutManager =
        CenterLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)

    init {
        recyclerView.setRecycledViewPool(this.viewPool)
        recyclerView.layoutManager = layoutManager
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = HorizontalAdapter(onChildListeners = this.onChildListeners)

        val startSnapHelper = PagerSnapHelper()
        startSnapHelper.attachToRecyclerView(recyclerView)
    }

    fun bind(item: ParentTwo) {
        headingText.text = item.toString()
        (recyclerView.adapter as HorizontalAdapter).submitList(item.items)

        if (item.collapsed) {
            collapseImage.setImageResource(R.drawable.ic_expand)
            recyclerView.visibility = View.GONE
        } else {
            collapseImage.setImageResource(R.drawable.ic_collapse)
            recyclerView.visibility = View.VISIBLE
        }

        collapseImage.setOnClickListener {
            onParentListeners.onCollapseExpandClicked(item)
        }
    }

    fun bindChange(item: ParentTwo) {
        headingText.text = item.toString()
        (recyclerView.adapter as HorizontalAdapter).submitList(item.items)

        if (item.collapsed) {
            collapseImage.setImageResource(R.drawable.ic_expand)
            recyclerView.visibility = View.GONE
        } else {
            collapseImage.setImageResource(R.drawable.ic_collapse)
            recyclerView.visibility = View.VISIBLE
        }

        collapseImage.setOnClickListener {
            onParentListeners.onCollapseExpandClicked(item)
        }
    }
}