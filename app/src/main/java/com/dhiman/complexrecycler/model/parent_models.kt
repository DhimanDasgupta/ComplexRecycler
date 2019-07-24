package com.dhiman.complexrecycler.model

sealed class BaseParent(open val id : Int)

data class ParentOne(override val id: Int, val items: List<BaseChild>): BaseParent(id)

data class ParentTwo(override val id: Int, val items: List<BaseChild>): BaseParent(id)