package com.example.android.politicalpreparedness.data

import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.data.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse
import retrofit2.http.Path

interface ElectionDataRemoteSource {

    suspend fun getElections(@Path("apikey") key: String): List<Election>

    suspend fun getVoterInfo(@Path("address") address: String, @Path("apiKey") key: String): List<VoterInfoResponse>

    suspend fun getRepresentativesByAddress(@Path("address") address: String, @Path("apiKey") key: String): List<RepresentativeResponse>
}