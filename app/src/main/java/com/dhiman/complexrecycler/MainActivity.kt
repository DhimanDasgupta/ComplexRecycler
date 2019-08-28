package com.dhiman.complexrecycler

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dhiman.complexrecycler.adapter.VerticalAdapter
import com.dhiman.complexrecycler.adapter.listeners.OnChildListeners
import com.dhiman.complexrecycler.adapter.listeners.OnParentListeners
import com.dhiman.complexrecycler.model.BaseChild
import com.dhiman.complexrecycler.model.BaseParent
import com.flaviofaria.kenburnsview.KenBurnsView

class MainActivity : AppCompatActivity(), OnParentListeners, OnChildListeners {
    private val kenBurnsView: KenBurnsView
        get() = findViewById(R.id.activity_recycler_kb_view)

    private val recyclerView: RecyclerView
        get() = findViewById(R.id.activity_recycler_view)
    private lateinit var verticalAdapter: VerticalAdapter

    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainActivityViewModel = ViewModelProvider(
            viewModelStore,
            MainActivityViewModelFactory
        ).get(MainActivityViewModel::class.java)

        recyclerView.also {
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(it.context, RecyclerView.VERTICAL, false)
            verticalAdapter = VerticalAdapter(onParentListeners = this, onChildListeners = this)
            it.adapter = verticalAdapter
        }

        mainActivityViewModel.listLiveData.observe(this, Observer {
            it?.let {
                verticalAdapter.submitList(it)
            }
        })
    }

    override fun onResume() {
        super.onResume()

        kenBurnsView.resume()
    }

    override fun onPause() {
        super.onPause()

        kenBurnsView.pause()
    }

    override fun onCollapseExpandClicked(baseParent: BaseParent) {
        mainActivityViewModel.onCollapseExpandClicked(baseParent, withDelay = false)

        Toast.makeText(this, "$baseParent", Toast.LENGTH_SHORT).show()
    }

    override fun onChildClicked(baseChild: BaseChild) {
        Toast.makeText(this, "$baseChild", Toast.LENGTH_SHORT).show()
    }

    override fun onToggleBookmark(baseChild: BaseChild) {
        mainActivityViewModel.onBookmarkClicked(baseChild, withDelay = false)
    }
}
