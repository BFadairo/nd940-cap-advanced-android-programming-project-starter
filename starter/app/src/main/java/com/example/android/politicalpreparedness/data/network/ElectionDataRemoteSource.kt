package com.example.android.politicalpreparedness.data.network

import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.data.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.data.network.models.Result
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse
import retrofit2.http.Path
import retrofit2.http.Query

interface ElectionDataRemoteSource {

    suspend fun getElections(): Result<List<Election>>

    suspend fun getVoterInfo(address: String, electionId: Long?): Result<VoterInfoResponse>

    suspend fun getRepresentativesByAddress(address: String): Result<RepresentativeResponse>
}