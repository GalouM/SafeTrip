package com.galou.watchmyback.data.source.local.dao

import androidx.room.*
import com.galou.watchmyback.data.source.database.WatchMyBackDatabase
import com.galou.watchmyback.data.entity.User
import com.galou.watchmyback.data.entity.UserPreferences
import com.galou.watchmyback.data.entity.UserWithPreferences
import com.galou.watchmyback.utils.*

/**
 * List all the actions possible on the [User] table
 *
 * @see Dao
 * @see User
 *
 * @author Galou Minisini
 *
 */
@Dao
abstract class UserDao(private val database: WatchMyBackDatabase) {

    /**
     * Query a [User] with a specific ID
     *
     * @param userId ID to query
     * @return [User] object with the ID requested
     *
     * @see Query
     */
    @Query("SELECT * FROM $USER_TABLE_NAME WHERE $USER_TABLE_UUID = :userId")
    abstract suspend fun getUser(userId: String): User?

    @Transaction
    @Query("SELECT * FROM $USER_TABLE_NAME WHERE $USER_TABLE_UUID = :userId")
    abstract suspend fun getUserWithPreferences(userId: String): UserWithPreferences?

    /**
     * Query a list of [User] who have a specific chain of character in their username
     *
     * @param username chain of character to query
     * @return List of [User] wo have the chain of character requested in their username
     *
     * @see Query
     */
    @Query("SELECT * FROM $USER_TABLE_NAME WHERE $USER_TABLE_USERNAME LIKE :username")
    abstract suspend fun getUsersFromUsername(username: String): List<User>

    /**
     * Create a [User] object in the database
     *
     * If an object with the same Primary key exist in the database, it will be replace by this one
     *
     * @param user [User] to create
     *
     * @see OnConflictStrategy.REPLACE
     * @see Insert
     *
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun createUser(user: User)


    /**
     * Update the [User] in the database
     *
     * @param user [User to Update]
     *
     * @see Update
     */
    @Update
    abstract suspend fun updateUser(user: User)

    /**
     * Delete [User] from the database
     *
     * @param user user to delete
     *
     * @see Delete
     */
    @Delete
    abstract suspend fun deleteUser(user: User)

    @Transaction
    open suspend fun createUserAndPreferences(user: User, preferences: UserPreferences){
        createUser(user)
        database.userPreferencesDao().createUserPreferences(preferences)
    }


}