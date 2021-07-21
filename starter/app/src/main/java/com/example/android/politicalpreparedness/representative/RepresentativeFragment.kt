package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.data.network.models.Address
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListener
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class DetailFragment : Fragment(), AdapterView.OnItemSelectedListener {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 0
    }

    private val representativeViewModel: RepresentativeViewModel by viewModel()
    private lateinit var representativeAdapter: RepresentativeListAdapter

    private lateinit var binding: FragmentRepresentativeBinding

    private lateinit var address: Address

    private lateinit var locationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_representative, container, false)
        binding.representativeViewModel = representativeViewModel
        binding.lifecycleOwner = this
        setupSpinner()
        setupRepresentativeAdapter()

        locationClient = getFusedLocationProviderClient(requireContext())

        representativeViewModel.representativeList.observe(
            viewLifecycleOwner,
            Observer { representatives ->
                representativeAdapter.submitList(representatives)
            })

        representativeViewModel.eventSearch.observe(viewLifecycleOwner, Observer { clicked ->
            if (clicked) {
                representativeViewModel.validateAddress()
                representativeViewModel.eventSearchFinished()
            }
        })

        representativeViewModel.eventGetMyLocation.observe(viewLifecycleOwner, Observer { clicked ->
            if (clicked) {
                checkLocationPermissions()
                representativeViewModel.eventGetMyLocationFinished()
            }
        })

        representativeViewModel.toastMessage.observe(viewLifecycleOwner, Observer { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        })

        return binding.root
    }

    private fun setupRepresentativeAdapter() {
        representativeAdapter = RepresentativeListAdapter(RepresentativeListener {
            Toast.makeText(requireContext(), "Hello", Toast.LENGTH_SHORT).show()
        })

        binding.representativeRecycler.adapter = representativeAdapter

        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        binding.representativeRecycler.layoutManager = layoutManager
    }

    private fun setupSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.states,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
            binding.state.adapter = adapter
        }

        binding.state.onItemSelectedListener = this
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
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
                representativeViewModel.validateGeocodeLocationAddress(address)
            }
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

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val state = binding.state.getItemAtPosition(position) as String
        representativeViewModel.setState(state)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}