package com.example.android.politicalpreparedness.data.local

import android.util.Log
import com.example.android.politicalpreparedness.data.local.database.ElectionDao
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.data.network.models.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class LocalRepository internal constructor(
    private val electionDao: ElectionDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ElectionDataLocalSource {
    override suspend fun insertElection(election: Election) {
        try {
            electionDao.insertElection(election)
        } catch (e: Exception) {
            Log.i("LocalDataSource", "Error Saving Election: ${e.message}")
        }
    }

    override suspend fun getAllElections(): Result<List<Election>> = withContext(ioDispatcher) {
        try {
            val elections = electionDao.getAllElections()
            return@withContext Result.Success(elections)
        } catch (e: Exception) {
            return@withContext Result.Error(e.message)
        }
    }

    override suspend fun getElectionById(id: Int): Result<Election> = withContext(ioDispatcher){
        try {
            val election = electionDao.getElectionById(id)
            return@withContext Result.Success(election)
        } catch (e: Exception) {
            return@withContext Result.Error(e.message)
        }
    }

    override suspend fun deleteElection(id: Int) {
        try {
            electionDao.deleteElection(id)
        } catch (e: Exception) {
            Log.e("LocalDataSource", "Error Deleting Election: ${e.message}")
        }
    }

    override suspend fun clear() {
        try {
            electionDao.clear()
        } catch (e: Exception) {
            Log.e("LocalDataSource", "Error clearing database: ${e.message}")
        }
    }
}