package ru.andrewvhub.horseracing.ui.viewHolders

import androidx.viewbinding.ViewBinding
import ru.andrewvhub.horseracing.core.BaseViewHolder
import ru.andrewvhub.horseracing.ui.items.Item

class SimpleViewHolder<T : Item>(viewBinding: ViewBinding) : BaseViewHolder<T>(viewBinding.root) {
    override fun bind(item: T) {}
}