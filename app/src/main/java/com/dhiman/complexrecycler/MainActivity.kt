package com.dhiman.complexrecycler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dhiman.complexrecycler.adapter.VerticalAdapter
import com.dhiman.complexrecycler.model.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rootList = mutableListOf<BaseParent>()
        for (i in 0..10) {
            if (i % 2 == 0) {
                rootList.add(
                    ParentOne(
                        i,
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
                        listOf(
                            ChildOne(i, "Item $i"),
                            ChildTwo(i + 1, "Item ${(i + 1)}"),
                            ChildOne(i + 2, "Item ${(i + 2)}"),
                            ChildTwo(i + 3, "Item ${(i + 3)}")
                        )
                    )
                )
            }
        }

        val recyclerView: RecyclerView = findViewById(R.id.activity_recycler_view)
        recyclerView.also {
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(it.context, RecyclerView.VERTICAL, false)
            it.adapter = VerticalAdapter(items = rootList)
        }
    }
}
