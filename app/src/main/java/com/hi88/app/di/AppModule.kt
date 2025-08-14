package com.hi88.app.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.hi88.app.generate_number.GenerateNumberViewModel
import com.hi88.app.history.data.repository.HistoryRepository
import com.hi88.app.history.data.repository.SavedRangesRepository
import com.hi88.app.list.data.ListDatabase
import com.hi88.app.list.data.repository.ListRepository
import com.hi88.app.list.presentation.ListViewModel
import com.hi88.app.role_dice.DiceViewModel
import com.hi88.app.settings.data.SettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val appModule = module {

    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create(
            produceFile = { androidContext().preferencesDataStoreFile("app_preferences") }
        )
    }

    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create(
            produceFile = { androidContext().preferencesDataStoreFile("settings") }
        )
    }

    single {
        HistoryRepository(get())
    }

    single {
        Room.databaseBuilder(androidContext(), ListDatabase::class.java, "list_db").build()
    }

    single {
        get<ListDatabase>().listDao()
    }

    single {
        ListRepository(get())
    }

    single {
        SavedRangesRepository(get())
    }

    single {
        SettingsRepository(get())
    }

    viewModel {
        DiceViewModel(get())
    }

    viewModel {
        ListViewModel(get())
    }

    viewModel {
        GenerateNumberViewModel(get(), get(), get())
    }
}