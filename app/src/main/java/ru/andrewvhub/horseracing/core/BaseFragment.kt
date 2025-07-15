package ru.andrewvhub.horseracing.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.andrewvhub.horseracing.ui.MainNavigator
import ru.andrewvhub.utils.extensions.navigateSafe
import ru.andrewvhub.utils.extensions.nonNullObserve

abstract class BaseFragment(private val layoutResId: Int) : Fragment() {

    protected open val viewModel: BaseViewModel? = null

    val mainNavigator
        get() = (activity as? MainNavigator)?.getNavController()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutResId, container, false).also {
        viewModel?.mainNavigate?.nonNullObserve(viewLifecycleOwner) {
            mainNavigator?.navigateSafe(it)
        }
    }
}