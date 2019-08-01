package com.dhiman.complexrecycler.adapter.listeners

import com.dhiman.complexrecycler.model.BaseChild

interface OnChildInteractionListener

interface OnChildListeners : OnChildInteractionListener {
    fun onChildClicked(baseChild: BaseChild)

    fun onToggleBookmark(baseChild: BaseChild)
}