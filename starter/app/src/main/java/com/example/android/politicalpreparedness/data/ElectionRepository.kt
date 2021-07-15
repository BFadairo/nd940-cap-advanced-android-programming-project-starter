package com.example.android.politicalpreparedness.data

import android.app.Application
import com.example.android.politicalpreparedness.data.local.LocalDataSource
import com.example.android.politicalpreparedness.data.local.database.ElectionDao
import com.example.android.politicalpreparedness.data.network.CivicsApiService
import com.example.android.politicalpreparedness.data.network.RemoteDataSource
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.data.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class ElectionRepository private constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSouce: LocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) {


}