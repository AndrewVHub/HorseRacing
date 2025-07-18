package ru.andrewvhub.horseracing.ui.items

interface Item {
    val id: String
    fun getType(): ItemContentType
    fun areItemsSame(newItem: Item): Boolean = getType() == newItem.getType() && id == newItem.id
    fun areContentsSame(newItem: Item): Boolean = equals(newItem)
    fun getChangePayload(newItem: Item): Any? = null
}