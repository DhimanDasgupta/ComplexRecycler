package com.dhiman.complexrecycler.model

sealed class BaseChild(open val id: Int)

data class ChildOne(override val id: Int, val name: String): BaseChild(id)

data class ChildTwo(override val id: Int, val name: String): BaseChild(id)