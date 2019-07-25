package com.dhiman.complexrecycler.adapter.listeners

import com.dhiman.complexrecycler.model.BaseParent

interface OnParentInteractionListener

interface OnParentListeners : OnParentInteractionListener {
    fun onCollapseExpandClicked(baseParent: BaseParent)
}