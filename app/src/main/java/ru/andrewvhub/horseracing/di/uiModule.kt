package ru.andrewvhub.horseracing.di

import androidx.recyclerview.widget.DiffUtil
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.andrewvhub.horseracing.ui.fragments.history.HistoryViewModel
import ru.andrewvhub.horseracing.ui.fragments.racing.RacingViewModel
import ru.andrewvhub.horseracing.ui.items.Item
import ru.andrewvhub.horseracing.ui.items.ItemCallback
import ru.andrewvhub.utils.adapter.Adapter

val uiModule = module {
    viewModel { RacingViewModel(get(),get(),get()) }
    viewModel { HistoryViewModel(get()) }


    factory<DiffUtil.ItemCallback<Item>> { ItemCallback() }
    factory { Adapter(get()) }
}