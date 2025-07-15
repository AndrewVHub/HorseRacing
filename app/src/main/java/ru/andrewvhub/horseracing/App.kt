package ru.andrewvhub.horseracing

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.context.startKoin
import ru.andrewvhub.horseracing.di.appModule
import ru.andrewvhub.horseracing.di.repositoryModule
import ru.andrewvhub.horseracing.di.uiModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            fragmentFactory()
            modules(
                appModule,
                repositoryModule,
                uiModule
            )
        }
    }
}