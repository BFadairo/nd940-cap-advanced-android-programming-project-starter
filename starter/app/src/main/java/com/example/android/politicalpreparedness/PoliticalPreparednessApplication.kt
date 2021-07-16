package com.example.android.politicalpreparedness

import android.app.Application
import com.example.android.politicalpreparedness.data.ElectionRepository
import com.example.android.politicalpreparedness.data.local.ElectionDataLocalSource
import com.example.android.politicalpreparedness.data.local.LocalRepository
import com.example.android.politicalpreparedness.data.local.database.ElectionDao
import com.example.android.politicalpreparedness.data.local.database.ElectionDatabase
import com.example.android.politicalpreparedness.data.network.CivicsApi
import com.example.android.politicalpreparedness.data.network.CivicsApiService
import com.example.android.politicalpreparedness.data.network.RemoteRepository
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.election.ElectionsViewModel
import com.example.android.politicalpreparedness.election.VoterInfoViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class PoliticalPreparednessApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())


        val myModule = module {
            //Declare view models
            viewModel {
                ElectionsViewModel(
                    get() as ElectionRepository
                )
            }

            single {
                VoterInfoViewModel(
                    get() as ElectionRepository
                )
            }
            single { LocalRepository(get()) }
            single { RemoteRepository(get()) }
            single { CivicsApi.retrofitService as CivicsApiService }
            single { ElectionRepository(get(), get()) }
            single { ElectionDatabase.getInstance(applicationContext).electionDao as ElectionDao }
        }

        startKoin {
            androidContext(this@PoliticalPreparednessApplication)
            modules(myModule)
        }
    }
}