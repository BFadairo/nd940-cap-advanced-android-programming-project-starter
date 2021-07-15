package com.example.android.politicalpreparedness.data.network

import com.example.android.politicalpreparedness.data.ElectionDataLocalSource
import com.example.android.politicalpreparedness.data.ElectionDataRemoteSource
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.data.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class RemoteDataSource internal constructor(
    private val civicsApiService: CivicsApiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ElectionDataRemoteSource {
    override suspend fun getElections(key: String): List<Election> {
        TODO("Not yet implemented")
    }

    override suspend fun getVoterInfo(address: String, key: String): List<VoterInfoResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getRepresentativesByAddress(
        address: String,
        key: String
    ): List<RepresentativeResponse> {
        TODO("Not yet implemented")
    }
}