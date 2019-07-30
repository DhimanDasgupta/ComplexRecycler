package com.dhiman.complexrecycler

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhiman.complexrecycler.model.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {
    private val list: MutableList<BaseParent> = mutableListOf()

    private val mutableListLiveData = MutableLiveData<List<BaseParent>>().also {
        it.postValue(list)
    }
    val listLiveData: LiveData<List<BaseParent>> = mutableListLiveData

    init {
        viewModelScope.launch(IO) {
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

            viewModelScope.launch(Main) {
                mutableListLiveData.postValue(list)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()

        viewModelScope.cancel()
    }

    fun onCollapseExpandClicked(baseParent: BaseParent) {
        viewModelScope.launch(Default) {
            val newList = list.map {
                val collapsed = if (it.id != baseParent.id) it.collapsed else !baseParent.collapsed
                when (it) {
                    is ParentOne -> it.copy(collapsed = collapsed)
                    is ParentTwo -> it.copy(collapsed = collapsed)
                }
            }.toList()

            viewModelScope.launch(Main) {
                list.clear()
                list.addAll(newList)
                mutableListLiveData.postValue(list)
            }
        }
    }
}