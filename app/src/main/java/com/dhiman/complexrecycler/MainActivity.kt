package com.dhiman.complexrecycler

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dhiman.complexrecycler.adapter.VerticalAdapter
import com.dhiman.complexrecycler.adapter.listeners.OnChildListeners
import com.dhiman.complexrecycler.adapter.listeners.OnParentListeners
import com.dhiman.complexrecycler.model.*

class MainActivity : AppCompatActivity(), OnParentListeners, OnChildListeners {
    private var rootList = mutableListOf<BaseParent>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var verticalAdapter: VerticalAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rootList = mutableListOf()
        for (i in 0..50) {
            if (i % 2 == 0) {
                rootList.add(
                    ParentOne(
                        i,
                        false,
                        listOf(
                            ChildOne(i, "Item $i"),
                            ChildTwo(i + 1, "Item ${(i + 1)}"),
                            ChildOne(i + 2, "Item ${(i + 2)}"),
                            ChildTwo(i + 3, "Item ${(i + 3)}")
                        )
                    )
                )
            } else {
                rootList.add(
                    ParentTwo(
                        i,
                        false,
                        listOf(
                            ChildOne(i + 4, "Item $i"),
                            ChildTwo(i + 5, "Item ${(i + 1)}"),
                            ChildOne(i + 6, "Item ${(i + 2)}"),
                            ChildTwo(i + 7, "Item ${(i + 3)}")
                        )
                    )
                )
            }
        }

        recyclerView = findViewById(R.id.activity_recycler_view)
        recyclerView.also {
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(it.context, RecyclerView.VERTICAL, false)
            verticalAdapter = VerticalAdapter(onParentListeners = this, onChildListeners = this)
            it.adapter = verticalAdapter
            verticalAdapter.submitList(rootList)
        }
    }

    override fun onCollapseExpandClicked(baseParent: BaseParent) {
        Log.d("Hello old", rootList.toString())
        val newList = rootList.map {
            val collapsed = if (it.id != baseParent.id) it.collapsed else !baseParent.collapsed
            when (it) {
                is ParentOne -> it.copy(collapsed = collapsed)
                is ParentTwo -> it.copy(collapsed = collapsed)
            }
        }.toList()

        rootList.clear()
        rootList.addAll(newList)
        verticalAdapter.submitList(newList)
        Log.d("Hello new", newList.toString())

        Toast.makeText(this, "$baseParent", Toast.LENGTH_SHORT).show()
    }

    override fun onChildClicked(baseChild: BaseChild) {
        Toast.makeText(this, "$baseChild", Toast.LENGTH_SHORT).show()
    }

    override fun onChildLongClicked(baseChild: BaseChild) {

    }


}
