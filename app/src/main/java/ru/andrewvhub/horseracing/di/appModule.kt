package ru.andrewvhub.horseracing.di

import android.content.res.Resources
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {
    single<Resources> { androidApplication().applicationContext.resources }
}