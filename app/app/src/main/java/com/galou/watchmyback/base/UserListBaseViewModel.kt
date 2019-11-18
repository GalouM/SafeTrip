package com.galou.watchmyback.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.galou.watchmyback.R
import com.galou.watchmyback.data.entity.OtherUser
import com.galou.watchmyback.data.repository.FriendRepository
import com.galou.watchmyback.data.repository.UserRepository
import com.galou.watchmyback.utils.Result
import com.galou.watchmyback.utils.extension.removeCurrentUser
import com.galou.watchmyback.utils.extension.setIsMyFriend
import kotlinx.coroutines.launch

/**
 * @author galou
 * 2019-11-08
 */
/**
 * [ViewModel] for views showing a list of user
 *
 * Inherit from [BaseViewModel]
 *
 * @property userRepository [UserRepositoryImp] reference
 * @property friendRepository [FriendRepositoryImpl] reference
 */
abstract class UserListBaseViewModel(
    private val userRepository: UserRepository,
    private val friendRepository: FriendRepository
) : BaseViewModel() {

    private val _usersLD = MutableLiveData<List<OtherUser>>()
    val usersLD: LiveData<List<OtherUser>> = _usersLD

    protected val currentUser = userRepository.currentUser.value!!

    /**
     * Remove or add a friend to the current user
     *
     *
     * @param friend friend to add or remove
     *
     * @see FriendRepositoryImpl.addFriend
     * @see FriendRepositoryImpl.removeFriend
     */
    fun removeOrAddFriend(friend: OtherUser){
        _dataLoading.value = true
        viewModelScope.launch {
            when (friend.myFriend){
                true -> friendRepository.removeFriend(currentUser, friend.user.id)
                false -> friendRepository.addFriend(currentUser, friend.user)

            }
            showUsersList(usersLD.value!!)
        }

    }

    /**
     * Determine if an operation was successful and either display its data or show an error
     *
     * @param result [Result] of the operation
     *
     * @see showUsersList
     * @see showSnackBarMessage
     */
    protected fun fetchResultUsers(result: Result<List<OtherUser>>){
        when (result) {
            is Result.Success -> showUsersList(result.data)
            is Result.Error -> showSnackBarMessage(R.string.no_user)
            is Result.Canceled -> showSnackBarMessage(R.string.canceled)
        }
    }

    /**
     * Emit the list of user's the the view
     *
     * @param users lsit of users to display
     */
    private fun showUsersList(users: List<OtherUser>){
        users setIsMyFriend currentUser.friendsId
        _usersLD.value = users removeCurrentUser currentUser.id
        if(users.isEmpty()) showSnackBarMessage(R.string.no_user)
        _dataLoading.value = false
    }
}