package com.example.android.politicalpreparedness.data.local

import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.data.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.data.network.models.Result
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse
import retrofit2.http.Path

interface ElectionDataLocalSource {
    suspend fun insertElection(election: Election): Result<String>

    suspend fun getAllElections(): Result<List<Election>>

    suspend fun getElectionById(id: Long): Result<Election>

    suspend fun deleteElection(id: Long): Result<String>

    suspend fun clear(): Result<String>
}