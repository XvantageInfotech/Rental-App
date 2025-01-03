package com.xvantage.rental.ui.onboarding

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import com.xvantage.rental.R
import com.xvantage.rental.databinding.ActivityBoardingPermissionBinding
import com.xvantage.rental.ui.registration.SignUpActivity
import com.xvantage.rental.utils.AppPreference
import com.xvantage.rental.utils.libs.toggle.SwitchButton

class BoardingPermissionActivity : AppCompatActivity() {
    private lateinit var layoutBinding: ActivityBoardingPermissionBinding
    private lateinit var appPreference: AppPreference
    private var isMediaToggle: Boolean = false
    private var isContactToggle: Boolean = false

    companion object {
        private const val MEDIA_PERMISSION_REQUEST_CODE = 1001
        private const val CONTACT_PERMISSION_REQUEST_CODE = 1002
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        layoutBinding = ActivityBoardingPermissionBinding.inflate(layoutInflater)
        setContentView(layoutBinding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        initView()
        onClickEvents()
    }

    private fun onClickEvents() {
        layoutBinding.mediaToggle.isChecked = isMediaToggle
        layoutBinding.contactToggle.isChecked = isContactToggle

        layoutBinding.mediaToggle.setOnCheckedChangeListener(object : SwitchButton.OnCheckedChangeListener {
            override fun onCheckedChanged(view: SwitchButton?, isChecked: Boolean) {
                if (isChecked) {
                    if (checkMediaPermissions()) {
                        isMediaToggle = true
                        layoutBinding.mediaToggle.isChecked = true
                    } else {
                        requestPermissions(getMediaPermissions(), 1001) { granted ->
                            isMediaToggle = granted
                            layoutBinding.mediaToggle.isChecked = granted
                        }
                    }
                } else {
                    isMediaToggle = false
                    layoutBinding.mediaToggle.isChecked = false
                }
            }
        })


        layoutBinding.contactToggle.setOnCheckedChangeListener(object : SwitchButton.OnCheckedChangeListener {
            override fun onCheckedChanged(view: SwitchButton?, isChecked: Boolean) {
                if (isChecked) {
                    if (checkContactPermissions()) {
                        isContactToggle = true
                    } else {
                        requestPermissions(getContactsPermissions(), 1002) { granted ->
                            isContactToggle = granted
                            layoutBinding.contactToggle.isChecked = granted
                        }
                    }
                } else {
                    isContactToggle = false
                }
            }
        })



        layoutBinding.btnConfirm.setOnClickListener {
            if (isMediaToggle) {
                proceedWithAction()
            } else {
                Toast.makeText(this, "Please allow media permissions", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkMediaPermissions(): Boolean {
        val permissions = getMediaPermissions()
        return permissions.all { ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }
    }

    private fun checkContactPermissions(): Boolean {
        val permissions = getContactsPermissions()
        return permissions.all { ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }
    }

    private fun initView() {
        appPreference = AppPreference(this)
        layoutBinding.mediaToggle.isChecked = isMediaToggle
        layoutBinding.contactToggle.isChecked = isContactToggle
    }

    private fun getMediaPermissions(): List<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listOf(Manifest.permission.READ_MEDIA_IMAGES)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            listOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            listOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
    }

    private fun getContactsPermissions(): List<String> {
        return listOf(Manifest.permission.READ_CONTACTS)
    }

    private fun requestPermissions(
        permissions: List<String>,
        requestCode: Int,
        callback: (Boolean) -> Unit
    ) {
        if (permissions.all { ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }) {
            callback(true)
        } else {
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), requestCode)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val granted = grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }

        when (requestCode) {
            MEDIA_PERMISSION_REQUEST_CODE -> {
                isMediaToggle = granted
                layoutBinding.mediaToggle.isChecked = checkMediaPermissions() // Double-check permission state
            }
            CONTACT_PERMISSION_REQUEST_CODE -> {
                isContactToggle = granted
                layoutBinding.contactToggle.isChecked = checkContactPermissions() // Double-check permission state
            }
        }
    }

    private fun proceedWithAction() {
        val intent = Intent(this, SignUpActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        appPreference.setFirstTimePreview(true)
        startActivity(intent)
    }
}