package com.dhiman.complexrecycler.model


sealed class BaseParent(open val id: Int, open val collapsed: Boolean = true)

data class ParentOne(override val id: Int, override val collapsed: Boolean, val items: List<BaseChild>) :
    BaseParent(id, collapsed)

data class ParentTwo(override val id: Int, override val collapsed: Boolean, val items: List<BaseChild>) :
    BaseParent(id, collapsed)