package com.xvantage.rental.ui.onboarding

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import android.Manifest
import com.xvantage.rental.R
import com.xvantage.rental.databinding.ActivityBoardingPermissionBinding
import com.xvantage.rental.utils.AppPreference
import com.xvantage.rental.utils.libs.toggle.SwitchButton

class BoardingPermissionActivity : AppCompatActivity() {
    private lateinit var layoutBinding: ActivityBoardingPermissionBinding
    lateinit var appPreference: AppPreference
    private var isMediaToggle: Boolean = false
    private var isContactToggle: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        layoutBinding = DataBindingUtil.setContentView(this, R.layout.activity_boarding_permission)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        intiView()
        onClickEvents()
    }

    private fun onClickEvents() {
        layoutBinding.mediaToggle.isChecked = isMediaToggle
        layoutBinding.contactToggle.isChecked = isContactToggle

        layoutBinding.mediaToggle.setOnCheckedChangeListener(object :
            SwitchButton.OnCheckedChangeListener {
            override fun onCheckedChanged(view: SwitchButton?, isChecked: Boolean) {
                isMediaToggle = isChecked
            }
        })

        layoutBinding.contactToggle.setOnCheckedChangeListener(object :
            SwitchButton.OnCheckedChangeListener {
            override fun onCheckedChanged(view: SwitchButton?, isChecked: Boolean) {
                isContactToggle = isChecked
            }
        })
        layoutBinding.btnConfirm.setOnClickListener {
            handlePermissions()
        }
    }

    private fun intiView() {
        appPreference = AppPreference(this)
        layoutBinding.mediaToggle.isChecked = isMediaToggle
        layoutBinding.contactToggle.isChecked = isContactToggle

    }

    private fun handlePermissions() {
        val permissionsToRequest = mutableListOf<String>()

        if (isMediaToggle) {
            permissionsToRequest.addAll(getMediaPermissions())
        } else if (isContactToggle) {
            permissionsToRequest.addAll(getContactsPermissions())
        }

        if (permissionsToRequest.isNotEmpty()) {
            requestPermissions(permissionsToRequest)
        } else {
            proceedWithAction()
        }
    }

    private fun getMediaPermissions(): List<String> {
        val permissions = mutableListOf<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        return permissions
    }

    private fun getContactsPermissions(): List<String> {
        return listOf(Manifest.permission.READ_CONTACTS)
    }

    private fun requestPermissions(permissions: List<String>) {
        ActivityCompat.requestPermissions(this, permissions.toTypedArray(), 1001)
    }

    private fun proceedWithAction() {
        // Proceed with the next steps after permissions are granted
        // This could involve navigating to another activity or performing specific actions
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            proceedWithAction()
        } else {
            // Handle permission denial, show message or retry
            proceedWithAction()
        }
    }
    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
    }
}