package ru.andrewvhub.horseracing.ui.items

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.andrewvhub.horseracing.core.BaseViewHolder
import ru.andrewvhub.horseracing.databinding.ItemEmptyBinding
import ru.andrewvhub.horseracing.databinding.ItemRaceHistoryBinding
import ru.andrewvhub.horseracing.ui.viewHolders.RaceHistoryViewHolder

import ru.andrewvhub.horseracing.ui.viewHolders.SimpleViewHolder

enum class ItemContentType {
    EmptyItemType {
        override fun onCreateViewHolder(parent: ViewGroup): BaseViewHolder<*> =
            SimpleViewHolder<EmptyItem>(
                ItemEmptyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
    },
    HistoryItemType {
        override fun onCreateViewHolder(parent: ViewGroup): BaseViewHolder<*> =
            RaceHistoryViewHolder(
                ItemRaceHistoryBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
    };

    abstract fun onCreateViewHolder(parent: ViewGroup): BaseViewHolder<*>
}