package ru.andrewvhub.horseracing.core

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.andrewvhub.horseracing.ui.items.Item
import ru.andrewvhub.utils.adapter.ItemAdapter

abstract class BaseViewHolder<T : Item>(view: View) : RecyclerView.ViewHolder(view) {

    abstract fun bind(item: T)

    open fun update(item: T, payloads: Set<*>) {}

    @Suppress("UNCHECKED_CAST")
    fun getItem(): T? =
        (bindingAdapter as? ItemAdapter)?.getItemByPosition(absoluteAdapterPosition) as T?

    fun getItemByPosition(position: Int) = (bindingAdapter as? ItemAdapter)?.getItemByPosition(position)
}