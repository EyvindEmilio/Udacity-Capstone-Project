package com.example.android.politicalpreparedness.representative

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListener
import com.example.android.politicalpreparedness.representative.model.Representative
import com.example.android.politicalpreparedness.representative.model.RepresentativeViewModelFactory
import com.google.android.gms.location.LocationServices
import java.util.*

class DetailFragment : Fragment() {

    companion object {
        //TODO: Add Constant for Location request
        const val REQUEST_CODE_LOCATION = 433
        private val TAG = DetailFragment::class.java.simpleName
    }

    //TODO: Declare ViewModel
    private lateinit var representativeVM: RepresentativeViewModel
    lateinit var bind: FragmentRepresentativeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentRepresentativeBinding.inflate(inflater, container, false)
        //TODO: Establish bindings
        representativeVM = ViewModelProvider(
            viewModelStore,
            RepresentativeViewModelFactory(RepresentativeRepository())
        ).get(RepresentativeViewModel::class.java)
        bind.lifecycleOwner = viewLifecycleOwner
        bind.representativeVM = representativeVM

        //TODO: Define and assign Representative adapter
        val representativeListener = object : RepresentativeListener {
            override fun onClick(representative: Representative) {

            }
        }
        val adapter = RepresentativeListAdapter(representativeListener)

        //TODO: Populate Representative adapter
        bind.rvRepresentatives.adapter = adapter
        representativeVM.statesLD.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            val stateAdapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, it)
            bind.state.adapter = stateAdapter
        })

        representativeVM.setState(resources.getStringArray(R.array.states).toList())

        bind.state.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                representativeVM.setCurrentStatePosition(position)
            }
        }

        //TODO: Establish button listeners for field and location search
        bind.buttonLocation.setOnClickListener {
            hideKeyboard()
            if (checkLocationPermissions()) {
                getLocation()
            }
        }
        bind.buttonSearch.setOnClickListener {
            representativeVM.loadRepresentatives()
        }

        bind.executePendingBindings()
        bind.invalidateAll()
        return bind.root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //TODO: Handle location permission result to get location on permission granted
        if (REQUEST_CODE_LOCATION == requestCode) {
            if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            } else {
                Toast.makeText(
                    requireContext(),
                    R.string.location_permission_is_required,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun checkLocationPermissions(): Boolean {
        Log.d(TAG, "checkLocationPermissions()")
        return if (isPermissionGranted()) {
            true
        } else {
            //TODO: Request Location permissions
            requestPermissions(
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION
            )
            false
        }
    }

    private fun isPermissionGranted(): Boolean {
        Log.d(TAG, "isPermissionGranted()")
        //TODO: Check if permission is already granted and return (true = granted, false = denied/other)
        return ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        Log.d(TAG, "getLocation()")
        //TODO: Get location from LocationServices
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            Log.d(TAG, "addOnSuccessListener() location=$location")
            //TODO: The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
            if (location != null) {
                val address = geoCodeLocation(location)
                representativeVM.setAddress(address)
                representativeVM.setCurrentState(address.state)
                representativeVM.loadRepresentatives()
                bind.state.setSelection(representativeVM.getStatePosition())
            } else {
                Toast.makeText(requireContext(), R.string.cannot_get_location, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun geoCodeLocation(location: Location): Address {
        Log.d(TAG, "geoCodeLocation() location=$location")
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
            .map { address ->
                Address(
                    address.thoroughfare ?: "",
                    address.subThoroughfare ?: "",
                    address.locality ?: "",
                    address.adminArea ?: "",
                    address.postalCode ?: ""
                )
            }
            .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

}