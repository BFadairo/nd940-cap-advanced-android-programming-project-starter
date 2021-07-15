package com.example.android.politicalpreparedness.data.local.database

import androidx.room.*
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.data.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse
import retrofit2.http.GET
import retrofit2.http.Path

@Dao
interface ElectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertElection(election: Election)

    @Query("SELECT * from ELECTION_TABLE")
    fun getAllElections(): List<Election>

    @Query("SELECT * from election_table WHERE id = :id")
    fun getElectionById(id: Int): Election

    @Query("DELETE from election_table WHERE id = :id")
    fun deleteElection(id: Int)

    @Query("DELETE from election_table")
    fun clear()

}