package com.example.android.politicalpreparedness.data.local

import com.example.android.politicalpreparedness.data.ElectionDataLocalSource
import com.example.android.politicalpreparedness.data.local.database.ElectionDao
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.data.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class LocalDataSource internal constructor(
    private val electionDao: ElectionDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ElectionDataLocalSource {
    override suspend fun insertElection(election: Election) = withContext(ioDispatcher) {
        electionDao.insertElection(election)
    }

    override suspend fun getAllElections(): List<Election> = withContext(ioDispatcher){
        try {
            val elections = electionDao.getAllElections()
            if (elections != null && elections.isNotEmpty()) {
                return@withContext elections
            }
        }
    }

    override suspend fun getElectionById(id: Int): Election {
        TODO("Not yet implemented")
    }

    override suspend fun deleteElection(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun clear() {
        TODO("Not yet implemented")
    }
}