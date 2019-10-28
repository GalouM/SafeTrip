package com.galou.watchmyback.profileActivity

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.galou.watchmyback.Event
import com.galou.watchmyback.R
import com.galou.watchmyback.data.entity.User
import com.galou.watchmyback.data.repository.UserRepository
import com.galou.watchmyback.data.repository.UserRepositoryImpl
import com.galou.watchmyback.utils.extension.isCorrectEmail
import com.galou.watchmyback.utils.extension.isCorrectName
import com.galou.watchmyback.utils.extension.isCorrectPhoneNumber

/**
 * [ViewModel] of [ProfileActivity]
 *
 * Inherit from [ViewModel]
 *
 * @see ViewModel
 * @see ProfileActivity
 *
 *
 * @property userRepository [UserRepositoryImpl] reference
 */
class ProfileViewModel (val userRepository: UserRepository) : ViewModel(){

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarMessage: LiveData<Event<Int>> = _snackbarText

    val usernameLD = MutableLiveData<String>()

    val emailLD = MutableLiveData<String>()

    private val _pictureUrlLD = MutableLiveData<String>()
    val pictureUrlLD: LiveData<String> = _pictureUrlLD

    val phoneNumberLD = MutableLiveData<String>()

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _errorEmail = MutableLiveData<Int?>()
    val errorEmail: LiveData<Int?> = _errorEmail

    private val _errorUsername = MutableLiveData<Int?>()
    val errorUsername: LiveData<Int?> = _errorUsername

    private val _errorPhoneNumber = MutableLiveData<Int?>()
    val errorPhoneNumber: LiveData<Int?> = _errorPhoneNumber

    private val _dataSaved = MutableLiveData<Event<Boolean>>()
    val dataSaved: LiveData<Event<Boolean>> = _dataSaved

    private val _openPhotoLibrary = MutableLiveData<Event<Unit>>()
    val openPhotoLibrary: LiveData<Event<Unit>> = _openPhotoLibrary

    val user = userRepository.currentUser.value!!

    /**
     * Emit the user information when the view model is created
     *
     * @see UserRepository.currentUser
     */
    init {
        _dataLoading.value = true
        usernameLD.value = user.username
        emailLD.value = user.email
        _pictureUrlLD.value = user.pictureUrl
        phoneNumberLD.value = user.phoneNumber
        
        _dataLoading.value = false
    }

    /**
     * Actions when the user confirm he/she wants to update his/her information
     *
     * @see ProfileViewModel.updateUserInDB
     * @see ProfileViewModel.userInputCorrect
     *
     */
    fun updateUserInformation(){
        _dataLoading.value = true
        _errorEmail.value = null
        _errorPhoneNumber.value = null
        _errorUsername.value = null
        if (userInputCorrect()){
            updateUserInDB()
        } else {
            _dataLoading.value = false
        }


    }

    fun pickProfilePicture(){
        _openPhotoLibrary.value = Event(Unit)
    }

    fun fetchPicturePickedByUser(resultCode: Int, uri: Uri?){
        _dataLoading.value = true
        if (resultCode == RESULT_OK){
            uri?.let {
                downloadPictureToRemoteStorage(uri)
            }
        }

    }

    /**
     * Update the user information in the repository and the remote database
     *
     * @see UserRepository.updateUserInRemoteDB
     * @see UserRepository.currentUser
     *
     */
    private fun updateUserInDB(){
        user.phoneNumber = phoneNumberLD.value
        user.email = emailLD.value
        user.username = usernameLD.value
        userRepository.updateUserInRemoteDB(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    userRepository.currentUser.value = user
                    _dataSaved.value = Event(true)
                    showSnackBarMessage(R.string.info_updated)
                    _dataLoading.value = false
                } else {
                    showSnackBarMessage(R.string.fail_not_saved)
                    _dataLoading.value = false
                }
            }
    }

    /**
     * Check if the value enter by the user are conform to what we expect
     *
     * Emit an error if necessary
     *
     * @see String.isCorrectPhoneNumber
     * @see String.isCorrectEmail
     * @see String.isCorrectName
     *
     * @return true is the information are correct, false otherwise
     */
    private fun userInputCorrect(): Boolean {
        var infoCorrect = true
        if (usernameLD.value?.isCorrectName() == false){
            _errorUsername.value = R.string.incorrect_username
            infoCorrect = false
        }
        if (emailLD.value?.isCorrectEmail() == false){
            _errorEmail.value = R.string.incorrect_email
            infoCorrect = false
        }
        if (phoneNumberLD.value?.isCorrectPhoneNumber() == false) {
            _errorPhoneNumber.value = R.string.incorrect_phone_number
            infoCorrect = false
        }

        return infoCorrect

    }

    private fun downloadPictureToRemoteStorage(uriPicture: Uri){
        userRepository.uploadUserPictureToRemoteStorageAndGetUrl(uriPicture)
            .addOnCompleteListener {task ->
                if (task.isSuccessful){
                    user.pictureUrl = task.result.toString()
                    _pictureUrlLD.value = user.pictureUrl
                    userRepository.currentUser.value = user
                } else {
                    showSnackBarMessage(R.string.error_download_picture)
                }
                _dataLoading.value = false

            }
    }

    // UTILS
    private fun showSnackBarMessage(message: Int){
        _snackbarText.value = Event(message)
    }
}