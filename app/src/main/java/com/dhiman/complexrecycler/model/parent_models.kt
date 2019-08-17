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

fun List<BaseParent>.onCollapseExpandClicked(baseParent: BaseParent): List<BaseParent> {
    return this.map {
        val collapsed = if (it.id != baseParent.id) it.collapsed else !baseParent.collapsed
        when (it) {
            is ParentOne -> it.copy(collapsed = collapsed)
            is ParentTwo -> it.copy(collapsed = collapsed)
        }
    }.toList()
}

fun List<BaseParent>.onBookmarkClicked(baseChild: BaseChild): List<BaseParent> {
    return this.map { it ->
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
}