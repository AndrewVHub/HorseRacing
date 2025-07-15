package ru.andrewvhub.horseracing.ui.fragments.tabs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import ru.andrewvhub.horseracing.R
import ru.andrewvhub.horseracing.core.BaseFragment
import ru.andrewvhub.horseracing.databinding.FragmentTabsBinding
import ru.andrewvhub.horseracing.ui.fragments.history.HistoryFragment
import ru.andrewvhub.horseracing.ui.fragments.racing.RacingFragment
import ru.andrewvhub.horseracing.ui.fragments.racing.RacingViewModel
import ru.andrewvhub.horseracing.ui.viewBinding.viewBinding
import ru.andrewvhub.utils.extensions.addSystemTopSpace


class TabsFragment : BaseFragment(R.layout.fragment_tabs) {

    private val viewBinding by viewBinding(FragmentTabsBinding::bind)

    // Общая ViewModel для обоих табов
    private val sharedViewModel: RacingViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?): Unit = with(viewBinding) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.addSystemTopSpace(true)

        // Адаптер для двух фрагментов
        viewPager.adapter = object : FragmentStateAdapter(this@TabsFragment) {
            override fun getItemCount() = 2
            override fun createFragment(position: Int): Fragment = when (position) {
                0 -> RacingFragment()
                else -> HistoryFragment()
            }
        }

        // Привязываем табы к страницам
        TabLayoutMediator(tabLayout, viewPager) { tab, pos ->
            tab.text = if (pos == 0) "Гонки" else "История"
        }.attach()
    }

}