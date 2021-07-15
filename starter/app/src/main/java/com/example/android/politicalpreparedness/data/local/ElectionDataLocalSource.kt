package com.example.android.politicalpreparedness.data.local

import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.data.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.data.network.models.Result
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse
import retrofit2.http.Path

interface ElectionDataLocalSource {
    suspend fun insertElection(election: Election)

    suspend fun getAllElections(): Result<List<Election>>

    suspend fun getElectionById(id: Int): Result<Election>

    suspend fun deleteElection(id: Int)

    suspend fun clear()
}