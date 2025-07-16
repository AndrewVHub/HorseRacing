package ru.andrewvhub.horseracing.ui.fragments.history

import android.os.Bundle
import android.view.View
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.andrewvhub.horseracing.R
import ru.andrewvhub.horseracing.core.BaseFragment
import ru.andrewvhub.horseracing.databinding.FragmentHistoryBinding
import ru.andrewvhub.horseracing.ui.itemDecorator.LinearLayoutItemDecorator
import ru.andrewvhub.horseracing.ui.viewBinding.viewBinding
import ru.andrewvhub.utils.adapter.Adapter

class HistoryFragment : BaseFragment(R.layout.fragment_history) {

    private val viewBinding by viewBinding(FragmentHistoryBinding::bind)
    override val viewModel by viewModel<HistoryViewModel>()

    private val adapterHistory: Adapter by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?): Unit = with(viewBinding) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.apply {
            adapter = adapterHistory
            addItemDecoration(
                LinearLayoutItemDecorator(
                    top = resources.getDimensionPixelSize(R.dimen.spacing_extra_small),
                    left = resources.getDimensionPixelSize(R.dimen.space_common_side),
                    right = resources.getDimensionPixelSize(R.dimen.space_common_side),
                    divider = resources.getDimensionPixelSize(R.dimen.spacing_small)
                )
            )
        }

        viewModel.raceHistoryItems.observe(viewLifecycleOwner) {
            adapterHistory.setCollection(it) {
                recyclerView.smoothScrollToPosition(0)
            }
        }
    }
}