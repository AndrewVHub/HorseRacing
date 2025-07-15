package ru.andrewvhub.horseracing.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import org.koin.androidx.fragment.android.setupKoinFragmentFactory
import ru.andrewvhub.horseracing.R
import ru.andrewvhub.horseracing.databinding.ActivityMainBinding
import ru.andrewvhub.horseracing.ui.MainNavigator
import ru.andrewvhub.horseracing.ui.viewBinding.viewBinding

class MainActivity : AppCompatActivity(), MainNavigator {

    private val viewBinding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_HorseRacing)
        enableEdgeToEdge()
        setupKoinFragmentFactory()
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
    }

    override fun getNavController(): NavController = findNavController(R.id.mainFragmentContainer)
}