package com.example.android.politicalpreparedness.data

import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.data.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse
import retrofit2.http.Path

interface ElectionDataLocalSource {
    suspend fun insertElection(election: Election)

    suspend fun getAllElections(): List<Election>

    suspend fun getElectionById(id: Int): Election

    suspend fun deleteElection(id: Int)

    suspend fun clear()
}