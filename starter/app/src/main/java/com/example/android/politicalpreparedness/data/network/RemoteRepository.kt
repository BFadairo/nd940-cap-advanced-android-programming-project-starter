package com.example.android.politicalpreparedness.data.network

import android.util.Log
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.data.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.data.network.models.Result
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class RemoteRepository internal constructor(
    private val civicsApiService: CivicsApiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ElectionDataRemoteSource {

    override suspend fun getElections(): Result<List<Election>> = withContext(ioDispatcher) {
        try {
            val elections = civicsApiService.getElections()
            return@withContext Result.Success(elections)
        } catch (e: HttpException) {
            Log.e("RemoteDataSource", "Error fetching elections: ${e.message()}")
            return@withContext Result.Error("Could not retrieve elections from remote data source: ${e.message()}")
        }
    }

    override suspend fun getVoterInfo(
        address: String,
        electionId: Long?
    ): Result<VoterInfoResponse> = withContext(ioDispatcher) {
        try {
            val voterInfoResponse = civicsApiService.getVoterInfo(address, electionId)
            return@withContext Result.Success(voterInfoResponse)
        } catch (e: HttpException) {
            Log.e(
                "RemoteDataSource",
                "Error fetching voter info from remote data source: ${e.message()}"
            )
            return@withContext Result.Error("Could not retrieve elections from remote data source ${e.message()}")
        }
    }

    override suspend fun getRepresentativesByAddress(address: String): Result<RepresentativeResponse> =
        withContext(ioDispatcher) {
            try {
                val representatives = civicsApiService.getRepresentativesByAddress(address)
                return@withContext Result.Success(representatives)
            } catch (e: HttpException) {
                Log.e(
                    "RemoteDataSource",
                    "Error fetching representatives from remote data source: ${e.message()}"
                )
                return@withContext Result.Error("Could not retrieve representatives from remote data source: ${e.message()}")
            }
        }
}