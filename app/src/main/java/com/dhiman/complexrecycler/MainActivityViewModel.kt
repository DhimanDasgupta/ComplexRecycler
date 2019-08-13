package com.dhiman.complexrecycler

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhiman.complexrecycler.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class MainActivityViewModel : ViewModel() {
    private val list: MutableList<BaseParent> = mutableListOf()

    private val mutableListLiveData = MutableLiveData<List<BaseParent>>().also {
        it.postValue(list)
    }
    val listLiveData: LiveData<List<BaseParent>> = mutableListLiveData

    init {
        fetchData()
    }

    override fun onCleared() {
        super.onCleared()

        viewModelScope.cancel()
    }

    private fun fetchData(withDelay: Boolean = false) {
        viewModelScope.launch(Main) {
            if (withDelay) {
                withContext(viewModelScope.coroutineContext + IO) {
                    delay(5000)
                    getInitialData()
                }
            } else {
                getInitialData()
            }

            showInitialData()
        }
    }

    private fun getInitialData() {
        for (i in 0..50) {
            if (i % 2 == 0) {
                list.add(
                    ParentOne(
                        i,
                        false,
                        listOf(
                            ChildOne(i, false, "Item $i"),
                            ChildTwo(i + 1, false, "Item ${(i + 1)}"),
                            ChildOne(i + 2, false, "Item ${(i + 2)}"),
                            ChildTwo(i + 3, false, "Item ${(i + 3)}")
                        )
                    )
                )
            } else {
                list.add(
                    ParentTwo(
                        i,
                        false,
                        listOf(
                            ChildOne(i + 4, false, "Item $i"),
                            ChildTwo(i + 5, false, "Item ${(i + 1)}"),
                            ChildOne(i + 6, false, "Item ${(i + 2)}"),
                            ChildTwo(i + 7, false, "Item ${(i + 3)}")
                        )
                    )
                )
            }
        }
    }

    private fun showInitialData() {
        mutableListLiveData.postValue(list)
    }

    fun onCollapseExpandClicked(baseParent: BaseParent, withDelay: Boolean = false) {
        viewModelScope.launch(Default) {

            if (withDelay) {
                withContext(viewModelScope.coroutineContext + Default) {
                    delay(2000)
                }
            }

            if (isActive) {
                cancel()
            }
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

    fun onBookmarkClicked(baseChild: BaseChild, withDelay: Boolean = false) {
        viewModelScope.launch(Default) {

            if (withDelay) {
                withContext(viewModelScope.coroutineContext + Default) {
                    delay(2000)
                }
            }


            if (isActive) {
                cancel()
            }
            val newList: List<BaseParent> = list.map { it ->
                if (it.items.contains(baseChild)) {
                    val child: MutableList<BaseChild> = mutableListOf()
                    it.items.forEach {
                        val bookmarked =
                            if (it.id != baseChild.id) it.bookmarked else !baseChild.bookmarked
                        when (it) {
                            is ChildOne -> child.add(it.copy(bookmarked = bookmarked))
                            is ChildTwo -> child.add(it.copy(bookmarked = bookmarked))
                        }
                    }

                    when (it) {
                        is ParentOne -> it.copy(items = child.toList())
                        is ParentTwo -> it.copy(items = child.toList())
                    }
                } else {
                    it
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