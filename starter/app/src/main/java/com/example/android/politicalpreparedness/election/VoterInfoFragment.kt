package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.data.ElectionRepository
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import org.koin.android.compat.ScopeCompat.viewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.java.KoinJavaComponent.inject

class VoterInfoFragment : Fragment() {

    private lateinit var binding: FragmentVoterInfoBinding

    private val voterInfoViewModel: VoterInfoViewModel by lazy {
        val electionRepository:ElectionRepository by inject()
        val electionId = VoterInfoFragmentArgs.fromBundle(requireArguments()).argElectionId
        val viewModelFactory = VoterInfoViewModelFactory(electionRepository, electionId.toLong())
        ViewModelProvider(this, viewModelFactory).get(VoterInfoViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_voter_info, container, false)
        binding.voterInfoViewModel = voterInfoViewModel
        binding.lifecycleOwner = this

        voterInfoViewModel.isSaved.observe(viewLifecycleOwner, Observer { isSaved ->
            if (isSaved) {
                binding.followElectionButton.text = resources.getString(R.string.unfollow_election_text)
            } else {
                binding.followElectionButton.text = resources.getString(R.string.follow_election_text)
            }
        })

        voterInfoViewModel.toastMessage.observe(viewLifecycleOwner, Observer { message ->
            if (message.isNotEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        })

        voterInfoViewModel.ballotInformationUrl.observe(viewLifecycleOwner, Observer { url ->
            if (url != null) {
                openBallotLocations(url)
                voterInfoViewModel.eventBallotInformationFinished()
            }
        })

        voterInfoViewModel.voterInformationUrl.observe(viewLifecycleOwner, Observer { url ->
            if (url != null) {
                openVotingLocations(url)
                voterInfoViewModel.eventVoterInformationFinished()
            }
        })
        return binding.root
    }

    private fun openVotingLocations(votingLocationsUrl: String) {
        val webpage: Uri = Uri.parse(votingLocationsUrl)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        startActivity(intent)
    }

    private fun openBallotLocations(ballotLocationsUrl: String) {
        val webpage: Uri = Uri.parse(ballotLocationsUrl)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        startActivity(intent)
    }

}