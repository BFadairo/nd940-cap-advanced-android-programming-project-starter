package com.example.android.politicalpreparedness.election

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.data.ElectionRepository
import com.example.android.politicalpreparedness.data.network.models.Address
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.example.android.politicalpreparedness.representative.DetailFragment
import com.google.android.gms.location.FusedLocationProviderClient
import org.koin.android.compat.ScopeCompat.viewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.java.KoinJavaComponent.inject
import java.util.*

class VoterInfoFragment : Fragment() {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 0
    }
    
    private lateinit var binding: FragmentVoterInfoBinding
    private lateinit var address: Address
    private lateinit var locationClient: FusedLocationProviderClient

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

        locationClient = FusedLocationProviderClient(requireContext())
        checkLocationPermissions()
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            } else {
                voterInfoViewModel.validateAddress(Address("Amphitheatre Parkway", "1600", "Mountain View", "California", "94043")) // Passing in Generic Address
                Toast.makeText(requireContext(), "WARNING: Without location access, We can not provide accurate voting information", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            getLocation()
            true
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            false
        }
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        locationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                address = geoCodeLocation(location)
                voterInfoViewModel.validateAddress(address)
            }
        }

        locationClient.lastLocation.addOnFailureListener {
            voterInfoViewModel.validateAddress(Address("Amphitheatre Parkway", "1600", "Mountain View", "California", "94043"))
        }
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
            .map { address ->
                Address(
                    address.thoroughfare,
                    address.subThoroughfare,
                    address.locality,
                    address.adminArea,
                    address.postalCode
                )
            }
            .first()
    }

}