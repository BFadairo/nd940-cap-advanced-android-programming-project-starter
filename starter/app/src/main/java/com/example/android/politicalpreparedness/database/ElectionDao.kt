package com.example.android.politicalpreparedness.database

import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

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