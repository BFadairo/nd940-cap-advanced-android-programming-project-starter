package com.example.android.politicalpreparedness.data

import com.example.android.politicalpreparedness.data.local.ElectionDataLocalSource
import com.example.android.politicalpreparedness.data.local.LocalRepository
import com.example.android.politicalpreparedness.data.network.ElectionDataRemoteSource
import com.example.android.politicalpreparedness.data.network.RemoteRepository
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.data.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.data.network.models.Result
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionRepository(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ElectionDataLocalSource,
    ElectionDataRemoteSource {

    override suspend fun insertElection(election: Election): Result<String> =
        withContext(ioDispatcher) {
            return@withContext localRepository.insertElection(election)
        }

    override suspend fun getAllElections(): Result<List<Election>> = withContext(ioDispatcher) {
        return@withContext localRepository.getAllElections()
    }

    override suspend fun getElectionById(id: Int): Result<Election> = withContext(ioDispatcher) {
        return@withContext localRepository.getElectionById(id)
    }

    override suspend fun deleteElection(id: Int): Result<String> = withContext(ioDispatcher) {
        return@withContext localRepository.deleteElection(id)
    }

    override suspend fun clear(): Result<String> = withContext(ioDispatcher) {
        return@withContext localRepository.clear()
    }

    override suspend fun getElections(): Result<List<Election>> = withContext(ioDispatcher) {
        return@withContext remoteRepository.getElections()
    }

    override suspend fun getVoterInfo(
        address: String,
        electionId: Long?
    ): Result<VoterInfoResponse> = withContext(ioDispatcher) {
        return@withContext remoteRepository.getVoterInfo(address, electionId)
    }

    override suspend fun getRepresentativesByAddress(address: String): Result<RepresentativeResponse> =
        withContext(ioDispatcher) {
            return@withContext remoteRepository.getRepresentativesByAddress(address)
        }

}