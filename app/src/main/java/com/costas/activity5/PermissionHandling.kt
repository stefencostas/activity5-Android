package com.costas.activity5

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.costas.activity5.databinding.ActivityPermissionHandlingBinding

class PermissionHandling : AppCompatActivity() {

    private lateinit var binding: ActivityPermissionHandlingBinding

    private val smsPermissionCode = 1
    private val phonestatePermissionCode = 2
    private val vibratePermissionCode = 3
    private val internetPermissionCode = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionHandlingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkAndSetTextPermissionStatus(
            Manifest.permission.READ_SMS,
            binding.SMSText,
            "SMS Access"
        )

        checkAndSetTextPermissionStatus(
            Manifest.permission.READ_PHONE_STATE,
            binding.PhoneStateText,
            "Phone State Access"
        )

        checkAndSetTextPermissionStatus(
            Manifest.permission.VIBRATE,
            binding.VibrateText,
            "Vibrate Access"
        )

        checkAndSetTextPermissionStatus(
            Manifest.permission.INTERNET,
            binding.InternetText,
            "Internet Access"
        )

        setupPermissionRequestButton(
            binding.SMSRequestButton,
            Manifest.permission.READ_SMS,
            smsPermissionCode
        )

        setupPermissionRequestButton(
            binding.PhoneStateRequestButton,
            Manifest.permission.READ_PHONE_STATE,
            phonestatePermissionCode
        )

        setupPermissionRequestButton(
            binding.VibrateRequestButton,
            Manifest.permission.VIBRATE,
            vibratePermissionCode
        )

        setupPermissionRequestButton(
            binding.InternetRequestButton,
            Manifest.permission.INTERNET,
            internetPermissionCode
        )

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun checkAndSetTextPermissionStatus(
        permission: String,
        textView: TextView,
        title: String
    ) {
        val statusText = if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            "$title: ALLOWED"
        } else {
            "$title: DENIED"
        }
        textView.text = statusText
    }

    private fun setupPermissionRequestButton(button: Button, permission: String, code: Int) {
        button.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                showGranted()
            } else {
                requestPermission(permission, code)
            }
        }
    }

    private fun requestPermission(permission: String, code: Int) {
        val rationaleMessage = when (permission) {
            Manifest.permission.READ_SMS -> "This permission is needed for the app to access SMS messages and provide relevant features."
            Manifest.permission.READ_PHONE_STATE -> "This permission is needed for the app to read phone state information, which may include the phone number, current cellular network information, etc."
            Manifest.permission.VIBRATE -> "This permission is needed for the app to vibrate the device for specific notifications or feedback."
            Manifest.permission.INTERNET -> "This permission is needed for the app to access the internet and retrieve or send data, ensuring proper functionality."
            else -> ""
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            showRationaleDialog(permission, code, rationaleMessage)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(permission), code)
        }
    }

    private fun showRationaleDialog(permission: String, code: Int, message: String) {
        AlertDialog.Builder(this).setTitle("Permission needed")
            .setMessage(message)
            .setPositiveButton("Okay") { _, _ ->
                ActivityCompat.requestPermissions(this, arrayOf(permission), code)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create().show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val permissionCodeMapping = mapOf(

            smsPermissionCode to binding.SMSText,
            phonestatePermissionCode to binding.PhoneStateText,
            vibratePermissionCode to binding.VibrateText,
            internetPermissionCode to binding.InternetText
        )

        val textView = permissionCodeMapping[requestCode]
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, PermissionHandling::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show()
        }

        textView?.let { checkAndSetTextPermissionStatus(permissions[0], it, "") }
    }

    private fun showGranted() {
        Toast.makeText(this, "Permission already Granted!", Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        super.onStop()

        // Reset permission status to "DENIED" when the application is closed
        resetPermissionStatus(binding.SMSText, "SMS Access")
        resetPermissionStatus(binding.PhoneStateText, "Phone State Access")
        resetPermissionStatus(binding.VibrateText, "Vibrate Access")
        resetPermissionStatus(binding.InternetText, "Internet Access")
    }

    private fun resetPermissionStatus(textView: TextView, title: String) {
        val statusText = "$title: DENIED"
        textView.text = statusText
    }
}
