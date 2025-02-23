package com.xvantage.rental.ui.auth

import android.app.Activity
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.xvantage.rental.R
import com.xvantage.rental.ui.auth.fragment.sealed.SignInResult

class GoogleAuthClient(
    private val activity: Activity
) {
    private val credentialManager = CredentialManager.create(activity)

    suspend fun signIn(): SignInResult {
        return try {
            // Use only the interactive sign-in flow
            val interactiveResponse = interactiveSignIn()
            processResponse(interactiveResponse)
        } catch (e: GetCredentialCancellationException) {
            e.printStackTrace()
            SignInResult.Cancelled
        } catch (e: NoCredentialException) {
            e.printStackTrace()
            SignInResult.NoCredentials
        } catch (e: GetCredentialException) {
            e.printStackTrace()
            SignInResult.Failure
        }
    }

    /**
     * Triggers an interactive sign-in, which displays the Google consent screen.
     */
    private suspend fun interactiveSignIn(): GetCredentialResponse {
        val getSignInWithGoogleOption = GetSignInWithGoogleOption.Builder(
            activity.getString(R.string.google_client_id)
        ).build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(getSignInWithGoogleOption)
            .build()

        return credentialManager.getCredential(
            context = activity,
            request = request
        )
    }

    /**
     * Processes the credential response and logs user details.
     */
    private fun processResponse(response: GetCredentialResponse): SignInResult {
        val credential = response.credential
        return if (credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            val tokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            Log.d("GoogleAuthClient", "Google ID: ${tokenCredential.id}")
            Log.d("GoogleAuthClient", "Display Name: ${tokenCredential.displayName.orEmpty()}")
            Log.d("GoogleAuthClient", "Profile Picture URL: ${tokenCredential.profilePictureUri}")
            Log.d("GoogleAuthClient", "Raw ID Token: ${credential.data}")

            SignInResult.Success(
                id = tokenCredential.id,
                username = tokenCredential.displayName.orEmpty(),
                avatarUrl = tokenCredential.profilePictureUri.toString()
            )
        } else {
            SignInResult.ErrorTypeCredentials
        }
    }
}
