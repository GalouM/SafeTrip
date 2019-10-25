package com.galou.watchmyback.testHelpers

import com.galou.watchmyback.data.entity.User
import com.galou.watchmyback.data.repository.UserRepository
import com.galou.watchmyback.data.repository.UserRepositoryImpl
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

/**
 * Created by galou on 2019-10-25
 */

class UserRepositoryMocked : UserRepository{

    override var currentUser: User? = null

    override fun getUserFromRemoteDB(userId: String): Task<User> = MockTask(generateTestUser(userId), true)

    override fun createUserInRemoteDB(user: User): Task<Void> = MockTask(null, true)

    override fun deleteUserFromCloudDB(userId: String): Task<Void> = MockTask(null, true)

    override fun uploadUserPicture(urlPicture: String, userId: String): UploadTask? = null
}