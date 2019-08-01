package com.dhiman.complexrecycler.model

sealed class BaseChild(open val id: Int, open val bookmarked: Boolean = false)

data class ChildOne(override val id: Int, override val bookmarked: Boolean, val name: String) :
    BaseChild(id, bookmarked)

data class ChildTwo(override val id: Int, override val bookmarked: Boolean, val name: String) :
    BaseChild(id, bookmarked)