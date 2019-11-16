package com.galou.watchmyback.data.repository

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import com.galou.watchmyback.data.entity.OtherUser
import com.galou.watchmyback.data.entity.User
import com.galou.watchmyback.data.entity.UserPreferences
import com.galou.watchmyback.data.entity.UserWithPreferences
import com.galou.watchmyback.testHelpers.URI_STORAGE_REMOTE
import com.galou.watchmyback.testHelpers.firstFriend
import com.galou.watchmyback.testHelpers.generateUserWithPref
import com.galou.watchmyback.testHelpers.secondFriend
import com.galou.watchmyback.utils.Result
import com.galou.watchmyback.utils.extension.toOtherUser

/**
 * Created by galou on 2019-10-25
 */

open class FakeUserRepositoryImpl : UserRepository{

    override val currentUser = MutableLiveData<User>()

    override val userPreferences= MutableLiveData<UserPreferences>()

    override suspend fun createNewUser(user: User): Result<Void?> = Result.Success(null)

    override suspend fun updateUser(user: User): Result<Void?> = Result.Success(null)

    override suspend fun deleteUser(user: User): Result<Void?> = Result.Success(null)

    override suspend fun fetchUser(userId: String): Result<UserWithPreferences?> = Result.Success(
        generateUserWithPref(userId)
    )

    override suspend fun updateUserPicture(user: User, internalUri: Uri): Result<Uri?> {
        val uriFromRemoteStorage = URI_STORAGE_REMOTE.toUri()
        return Result.Success(uriFromRemoteStorage)
    }

    override suspend fun updateUserPreferences(preferences: UserPreferences): Result<Void?> = Result.Success(null)

    override suspend fun fetchAllUsers(): Result<List<OtherUser>> = Result.Success(listOf(
        firstFriend.toOtherUser(false), secondFriend.toOtherUser(false)))

    override suspend fun fetchUserByUsername(name: String): Result<List<OtherUser>> = Result.Success(listOf(
        firstFriend.toOtherUser(false)))

    override suspend fun fetchUserByEmailAddress(emailAddress: String): Result<List<OtherUser>> = Result.Success(listOf(
        secondFriend.toOtherUser(false)))
}

