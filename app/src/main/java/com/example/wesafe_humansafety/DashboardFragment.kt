package com.example.wesafe_humansafety

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class DashboardFragment : Fragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        //return inflater.inflate(R.layout.fragment_dashboard, container, false)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getLocation()

        return view
    }
    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission granted, fetch the last known location
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    // Use the fetched location
                    handleLocation(latitude, longitude)
                } else {
                    // Handle null location
                    Toast.makeText(
                        requireContext(),
                        "Unable to fetch location. Try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            // Request location permission
            requestPermissions(
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )
        }
    }

    private fun handleLocation(latitude: Double, longitude: Double) {
        // Display the location data or use it as needed
        Toast.makeText(
            requireContext(),
            "Latitude: $latitude, Longitude: $longitude",
            Toast.LENGTH_LONG
        ).show()
        // Additional logic can go here, like sending location to a server or displaying it on a map
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQUEST_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission granted, fetch location
            getLocation()
        } else {
            // Permission denied
            Toast.makeText(
                requireContext(),
                "Location permission denied. Enable it in settings.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = DashboardFragment()
    }
}