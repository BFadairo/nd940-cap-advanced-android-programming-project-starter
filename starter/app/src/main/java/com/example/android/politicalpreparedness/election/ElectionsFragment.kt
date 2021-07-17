package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class ElectionsFragment: Fragment() {

    private val electionViewModel: ElectionsViewModel by viewModel()

    private lateinit var binding: FragmentElectionBinding

    private lateinit var electionsAdapter: ElectionListAdapter
    private lateinit var followedElectionsAdapter: ElectionListAdapter

    private lateinit var clickedElection: Election

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_election, container, false)
        binding.electionViewModel = electionViewModel
        electionViewModel.upcomingElections.observe(viewLifecycleOwner, Observer { list ->
            electionsAdapter.submitList(list)
        })

        electionViewModel.savedElections.observe(viewLifecycleOwner, Observer { list ->
            followedElectionsAdapter.submitList(list)
        })

        electionViewModel.eventNavigateToUpcomingElectionInfo.observe(viewLifecycleOwner, Observer { clicked ->
            if (clicked) {
                navigateToVoterInfo(clickedElection)
                electionViewModel.navigateToUpcomingElectionInfoFinished()
            }
        })

        electionViewModel.eventNavigateToSavedElectionInfo.observe(viewLifecycleOwner, Observer { clicked ->
            if (clicked) {
                navigateToVoterInfo(clickedElection)
                electionViewModel.navigateToSavedElectionInfoFinished()
            }
        })

        electionViewModel.upcomingElectionsLoading.observe(viewLifecycleOwner, Observer { loading ->
            if (loading) {
                // Setup a loading indicator
            }
        })

        electionViewModel.savedElectionsLoading.observe(viewLifecycleOwner, Observer { loading ->
            if (loading) {
                // Setup a loading indicator
            }
        })

        createElectionRecyclerAdapter()
        createFollowedElectionsRecycler()


        return binding.root
    }

    private fun navigateToVoterInfo(election: Election) {
        findNavController().navigate(
            ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(election.id, election.division)
        )
    }

    private fun createElectionRecyclerAdapter() {
        electionsAdapter = ElectionListAdapter(ElectionListener { election ->
            clickedElection = election
            electionViewModel.navigateToUpcomingElectionInfo()
        })

        binding.electionRecycler.adapter = electionsAdapter

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        binding.electionRecycler.layoutManager = layoutManager
    }

    private fun createFollowedElectionsRecycler() {
        followedElectionsAdapter = ElectionListAdapter(ElectionListener { election ->
            clickedElection = election
            electionViewModel.navigateToSavedElectionInfo()
        })

        binding.followedRecycler.adapter = followedElectionsAdapter

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        binding.followedRecycler.layoutManager = layoutManager
    }

    override fun onResume() {
        super.onResume()
        electionViewModel.retrieveUpcomingElections()
        electionViewModel.retrieveSavedElections()
    }
}