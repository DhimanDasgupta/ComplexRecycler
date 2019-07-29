package com.dhiman.complexrecycler

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dhiman.complexrecycler.model.*

class MainActivityViewModel : ViewModel() {
    private val list: MutableList<BaseParent> = mutableListOf()

    private val mutableListLiveData = MutableLiveData<List<BaseParent>>().also {
        it.postValue(list)
    }
    val listLiveData: LiveData<List<BaseParent>> = mutableListLiveData

    init {
        for (i in 0..50) {
            if (i % 2 == 0) {
                list.add(
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
                list.add(
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

        mutableListLiveData.postValue(list)
    }

    fun onCollapseExpandClicked(baseParent: BaseParent) {
        val newList = list.map {
            val collapsed = if (it.id != baseParent.id) it.collapsed else !baseParent.collapsed
            when (it) {
                is ParentOne -> it.copy(collapsed = collapsed)
                is ParentTwo -> it.copy(collapsed = collapsed)
            }
        }.toList()

        list.clear()
        list.addAll(newList)
        mutableListLiveData.postValue(list)
    }
}