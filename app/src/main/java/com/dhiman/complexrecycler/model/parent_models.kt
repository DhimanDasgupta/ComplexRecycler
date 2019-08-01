package com.dhiman.complexrecycler.model


sealed class BaseParent(
    open val id: Int,
    open val collapsed: Boolean = true,
    open val items: List<BaseChild>
)

data class ParentOne(
    override val id: Int,
    override val collapsed: Boolean,
    override val items: List<BaseChild>
) :
    BaseParent(id, collapsed, items)

data class ParentTwo(
    override val id: Int,
    override val collapsed: Boolean,
    override val items: List<BaseChild>
) :
    BaseParent(id, collapsed, items)