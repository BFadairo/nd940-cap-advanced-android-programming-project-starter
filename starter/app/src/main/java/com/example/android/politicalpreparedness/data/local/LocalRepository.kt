package com.example.android.politicalpreparedness.data.local

import android.util.Log
import com.example.android.politicalpreparedness.data.local.database.ElectionDao
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.data.network.models.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception

class LocalRepository internal constructor(
    private val electionDao: ElectionDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ElectionDataLocalSource {
    override suspend fun insertElection(election: Election): Result<String> = withContext(ioDispatcher) {
        try {
            electionDao.insertElection(election)
            return@withContext Result.Success("Election Successfully Saved")
        } catch (e: Exception) {
            Timber.e("Error Saving Election: ${e.message}")
            return@withContext Result.Error("There was an error saving the election: ${e.message}")
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

    override suspend fun deleteElection(id: Int): Result<String> = withContext(ioDispatcher) {
        try {
            electionDao.deleteElection(id)
            return@withContext Result.Success("Election successfully deleted from Database")
        } catch (e: Exception) {
            Timber.e("Error Deleting Election: ${e.message}")
            return@withContext Result.Error("There was a problem deleting the election: ${e.message}")
        }
    }

    override suspend fun clear(): Result<String> = withContext(ioDispatcher){
        try {
            electionDao.clear()
            return@withContext Result.Success("All elections successfully wiped from database")
        } catch (e: Exception) {
            Timber.e("Error clearing database: ${e.message}")
            return@withContext Result.Error("There was an error clearing the elections: ${e.message}")
        }
    }
}