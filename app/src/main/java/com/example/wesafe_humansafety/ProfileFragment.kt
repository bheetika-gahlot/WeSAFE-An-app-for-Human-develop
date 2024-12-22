package com.example.wesafe_humansafety

import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.net.Uri

class ProfileFragment : Fragment(), ShakeDetector.OnShakeListener {

    private val REQUEST_SMS_PERMISSION = 1
    private val REQUEST_CALL_PERMISSION = 2
    private val phoneNumber = "9717024185"  // Emergency contact's phone number
    private val policePhoneNumber = "112"  // police phone number
    private val sosMessage = "SOS! I need help. My location is: [Add Location]"

    private lateinit var sensorManager: SensorManager
    private lateinit var shakeDetector: ShakeDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Check for SMS permission
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.SEND_SMS), REQUEST_SMS_PERMISSION)
        }
        // Check for CALL permission
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CALL_PERMISSION)
        }
        // Initialize sensor manager and shake detector
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        shakeDetector = ShakeDetector(this)

        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI)


        val sosButton: Button = view.findViewById(R.id.sosButton)
        sosButton.setOnClickListener {
            sendSOSMessage(phoneNumber, sosMessage)
            //sendSOSMessage(policePhoneNumber, sosMessage)
        }
        val callButton: Button = view.findViewById(R.id.callButton)
        callButton.setOnClickListener {
            makePhoneCall(phoneNumber)
            //makePhoneCall(policePhoneNumber)
        }
        return view
    }
    private fun sendSOSMessage(phoneNumber: String, message: String) {
        try {
            val smsManager: SmsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            Toast.makeText(requireContext(), "SOS message sent!", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Failed to send SOS message.", Toast.LENGTH_LONG)
                .show()
        }
    }
    private fun makePhoneCall(phoneNumber: String) {
        try {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:$phoneNumber")
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Failed to make a call.", Toast.LENGTH_LONG).show()
        }
    }
    override fun onShake() {
        sendSOSMessage(phoneNumber, sosMessage)
        sendSOSMessage(policePhoneNumber, sosMessage)
        makePhoneCall(phoneNumber)
        makePhoneCall(policePhoneNumber)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_SMS_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission granted
                    Toast.makeText(requireContext(), "SMS Permission Granted", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    // Permission denied
                    Toast.makeText(requireContext(), "SMS Permission Denied", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            REQUEST_CALL_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission granted
                    Toast.makeText(requireContext(), "Call Permission Granted", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    // Permission denied
                    Toast.makeText(requireContext(), "Call Permission Denied", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
        companion object {

            @JvmStatic
            fun newInstance() = ProfileFragment()

        }
    }
//        if (requestCode == REQUEST_SMS_PERMISSION) {
//            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                // Permission granted
//                Toast.makeText(requireContext(), "SMS Permission Granted", Toast.LENGTH_SHORT).show()
//            } else {
//                // Permission denied
//                Toast.makeText(requireContext(), "SMS Permission Denied", Toast.LENGTH_SHORT).show()
//            }
//        }
