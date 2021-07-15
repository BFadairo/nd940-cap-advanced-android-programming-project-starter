package com.example.android.politicalpreparedness.data

import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.data.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse
import retrofit2.http.Path

interface ElectionDataSource {
    suspend fun insertElection(election: Election)

    suspend fun getAllElections(): List<Election>

    suspend fun getElectionById(id: Int): Election

    suspend fun deleteElection(id: Int)

    suspend fun clear()

    suspend fun getElections(@Path("apikey") key: String): List<Election>

    suspend fun getVoterInfo(@Path("address") address: String, @Path("apiKey") key: String): List<VoterInfoResponse>

    suspend fun getRepresentativesByAddress(@Path("address") address: String, @Path("apiKey") key: String): List<RepresentativeResponse>
}