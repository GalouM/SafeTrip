package com.galou.watchmyback.data.database

import android.content.Context
import androidx.room.*
import com.galou.watchmyback.data.database.dao.*
import com.galou.watchmyback.data.entity.*

/**
 * Represent the Database of the application
 *
 * Inherit from [RoomDatabase]
 *
 * Contain all the [Dao]
 *
 * Contain a companion object to create a single instance the database
 *
 * @see Database
 * @see RoomDatabase
 * @see TypeConverters
 * @see Dao
 *
 */
@Database(
    entities = [
        User::class, Trip::class, PointTrip::class, Location::class,
        WeatherData::class, CheckList::class, ItemCheckList::class, Friend::class, Watcher::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class WatchMyBackDatabase : RoomDatabase() {

    // --- DAO
    abstract fun checkListDao(): CheckListDao
    abstract fun friendDao(): FriendDao
    abstract fun itemCheckListDao(): ItemCheckListDao
    abstract fun userDao(): UserDao
    abstract fun watcherDao(): WatcherDao
    abstract fun weatherDataDao(): WeatherDataDao
    abstract fun locationDao(): LocationDao
    abstract fun pointTripDao(): PointTripDao
    abstract fun tripDao(): TripDao

    companion object{
        @Volatile
        private var INSTANCE: WatchMyBackDatabase? = null

        /**
         * Create a single Instance of the database
         *
         * @param context the application's context
         * @return unique instance of the database
         */
        fun getDatabase(context: Context): WatchMyBackDatabase {
            return INSTANCE
                ?: synchronized(this){
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        WatchMyBackDatabase::class.java,
                        "WatchMyBack_database.db")
                        .build()
                    INSTANCE = instance
                    return instance
                }
        }
    }
}