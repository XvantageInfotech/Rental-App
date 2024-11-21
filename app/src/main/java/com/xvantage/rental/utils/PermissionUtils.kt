package com.xvantage.rental.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

object PermissionUtils {
    const val REQUEST_LOCATION_SETTING = 1000
    const val RESULTCODE_PERMISSION_LOCATION = 1001
    const val RESULTCODE_PERMISSION_CAMERA = 1002
    const val RESULTCODE_PERMISSION_STORAGE = 1003
    const val RESULTCODE_PERMISSION_CONTACT = 1004
    const val RESULTCODE_PERMISSION_CALL = 1005

    const val REQUEST_MEDIA_PERMISSION = 1001
    const val REQUEST_CONTACT_PERMISSION = 1002

    private const val PERM_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    private const val PERM_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    private const val PERM_CAMERA = Manifest.permission.CAMERA
    private const val PERM_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
    private const val PERM_READ_CONTACTS = Manifest.permission.READ_CONTACTS
    private const val PERM_CALL_PHONE = Manifest.permission.CALL_PHONE

    private const val PERM_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
    private const val PERM_READ_MEDIA_IMAGES = Manifest.permission.READ_MEDIA_IMAGES


    private const val TAG = "PermissionUtils"

    /**
     * navigate to app setting - enable permission
     *
     * @param mActivity
     * @param message
     * @param requestCode
     */
    /*fun navigateToSettingPermissionDialog(mActivity: Activity, message: String, requestCode: Int) {
        DialogUtils.getInstance().showCustomYesNoAlertDialog(
            mActivity,
            false,
            message,
            mActivity.getString(R.string.go_to_setting),
            mActivity.getString(R.string.no),
            object : DialogUtils.OnDialogOkCancelButtonClickListener {
                override fun onOkButtonClick() {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", mActivity.packageName, null)
                    intent.data = uri
                    mActivity.startActivityForResult(intent, requestCode)
                }

                override fun onCancelButtonClick() {
                    // Do nothing on cancel
                }
            }
        )
    }*/

    /**
     * check location permission is granted, or not
     *
     * @param mActivity
     * @return
     */
    fun checkLocationPermission(mActivity: Activity): Boolean {
        return ContextCompat.checkSelfPermission(
            mActivity,
            PERM_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    mActivity,
                    PERM_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * check location permission is granted, or not
     *
     * @param mActivity
     * @param fragment
     * @param requestCode
     * @return
     */
    fun checkLocationPermission(
        mActivity: Activity,
        fragment: Fragment?,
        requestCode: Int
    ): Boolean {
        return if (ContextCompat.checkSelfPermission(
                mActivity,
                PERM_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                mActivity,
                PERM_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else {
            fragment?.requestPermissions(
                arrayOf(PERM_COARSE_LOCATION, PERM_FINE_LOCATION),
                requestCode
            )
                ?: ActivityCompat.requestPermissions(
                    mActivity,
                    arrayOf(PERM_COARSE_LOCATION, PERM_FINE_LOCATION),
                    requestCode
                )
            false
        }
    }


    /**
     * check location permission is granted, ask if not
     *
     * @param mActivity
     * @param fragment
     * @return
     */
    fun checkLocationPermissionNeverAskAgain(
        mActivity: Activity,
        fragment: Fragment? = null
    ): Boolean {
        return if (fragment != null) {
            fragment.shouldShowRequestPermissionRationale(PERM_COARSE_LOCATION) &&
                    fragment.shouldShowRequestPermissionRationale(PERM_FINE_LOCATION)
        } else {
            ActivityCompat.shouldShowRequestPermissionRationale(mActivity, PERM_COARSE_LOCATION) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        mActivity,
                        PERM_FINE_LOCATION
                    )
        }
    }

    /**
     * check camera permission is granted, ask if not
     *
     * @param mActivity
     * @param fragment
     * @param requestCode
     * @return
     */
    fun checkCameraPermission(mActivity: Activity, fragment: Fragment?, requestCode: Int): Boolean {
        return if (ContextCompat.checkSelfPermission(
                mActivity,
                PERM_CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else {
            fragment?.requestPermissions(arrayOf(PERM_CAMERA), requestCode)
                ?: ActivityCompat.requestPermissions(mActivity, arrayOf(PERM_CAMERA), requestCode)
            false
        }
    }

    /**
     * check camera permission is granted, ask if not
     *
     * @param mActivity
     * @param fragment
     * @return
     */
    fun checkCameraPermissionNeverAskAgain(
        mActivity: Activity,
        fragment: Fragment? = null
    ): Boolean {
        return fragment?.shouldShowRequestPermissionRationale(PERM_CAMERA)
            ?: ActivityCompat.shouldShowRequestPermissionRationale(mActivity, PERM_CAMERA)
    }

    /**
     * check storage permission is granted, ask if not
     *
     * @param mActivity
     * @param fragment
     * @param requestCode
     * @return
     */
    fun checkStoragePermission(
        mActivity: Activity,
        fragment: Fragment?,
        requestCode: Int
    ): Boolean {
        return if (ContextCompat.checkSelfPermission(
                mActivity,
                PERM_WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else {
            fragment?.requestPermissions(arrayOf(PERM_WRITE_EXTERNAL_STORAGE), requestCode)
                ?: ActivityCompat.requestPermissions(
                    mActivity,
                    arrayOf(PERM_WRITE_EXTERNAL_STORAGE),
                    requestCode
                )
            false
        }
    }

    /**
     * check storage permission is granted, ask if not
     *
     * @param mActivity
     * @param fragment
     * @return
     */
    fun checkStoragePermissionNeverAskAgain(
        mActivity: Activity,
        fragment: Fragment? = null
    ): Boolean {
        return fragment?.shouldShowRequestPermissionRationale(PERM_WRITE_EXTERNAL_STORAGE)
            ?: ActivityCompat.shouldShowRequestPermissionRationale(
                mActivity,
                PERM_WRITE_EXTERNAL_STORAGE
            )
    }

    /**
     * check contacts permission is granted, ask if not
     *
     * @param mActivity
     * @param fragment
     * @param requestCode
     * @return
     */
    fun checkContactsPermission(
        mActivity: Activity,
        fragment: Fragment? = null,
        requestCode: Int = REQUEST_CONTACT_PERMISSION
    ): Boolean {
        return if (ContextCompat.checkSelfPermission(
                mActivity,
                PERM_READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else {
            fragment?.requestPermissions(arrayOf(PERM_READ_CONTACTS), requestCode)
                ?: ActivityCompat.requestPermissions(
                    mActivity,
                    arrayOf(PERM_READ_CONTACTS),
                    requestCode
                )
            false
        }
    }

    /**
     * Check and request media permissions (READ/WRITE based on SDK version)
     */
    fun checkMediaPermissions(
        activity: Activity, fragment: Fragment? = null, requestCode: Int = REQUEST_MEDIA_PERMISSION
    ): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkPermissions(
                activity,
                fragment,
                arrayOf(PERM_READ_MEDIA_IMAGES),
                requestCode
            )
        } else {
            checkPermissions(
                activity,
                fragment,
                arrayOf(PERM_READ_EXTERNAL_STORAGE, PERM_WRITE_EXTERNAL_STORAGE),
                requestCode
            )
        }
    }

    /**
     * General permission check and request method
     */
   private fun checkPermissions(
        activity: Activity,
        fragment: Fragment?,
        permissions: Array<String>,
        requestCode: Int
    ): Boolean {
        val deniedPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }

        return if (deniedPermissions.isEmpty()) {
            true
        } else {
            fragment?.requestPermissions(deniedPermissions.toTypedArray(), requestCode)
                ?: ActivityCompat.requestPermissions(activity, deniedPermissions.toTypedArray(), requestCode)
            false
        }
    }
    /**
     * check contacts permission is granted, ask if not
     *
     * @param mActivity
     * @param fragment
     * @return
     */
    fun checkContactsPermissionNeverAskAgain(
        mActivity: Activity,
        fragment: Fragment? = null
    ): Boolean {
        return fragment?.shouldShowRequestPermissionRationale(PERM_READ_CONTACTS)
            ?: ActivityCompat.shouldShowRequestPermissionRationale(mActivity, PERM_READ_CONTACTS)
    }

    /**
     * check call permission is granted, ask if not
     *
     * @param mActivity
     * @param fragment
     * @param requestCode
     * @return
     */
    fun checkCallPermission(mActivity: Activity, fragment: Fragment?, requestCode: Int): Boolean {
        return if (ContextCompat.checkSelfPermission(
                mActivity,
                PERM_CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else {
            fragment?.requestPermissions(arrayOf(PERM_CALL_PHONE), requestCode)
                ?: ActivityCompat.requestPermissions(
                    mActivity,
                    arrayOf(PERM_CALL_PHONE),
                    requestCode
                )
            false
        }
    }

    /**
     * check call permission is granted, ask if not
     *
     * @param mActivity
     * @param fragment
     * @return
     */
    fun checkCallPermissionNeverAskAgain(mActivity: Activity, fragment: Fragment? = null): Boolean {
        return fragment?.shouldShowRequestPermissionRationale(PERM_CALL_PHONE)
            ?: ActivityCompat.shouldShowRequestPermissionRationale(mActivity, PERM_CALL_PHONE)
    }
}
