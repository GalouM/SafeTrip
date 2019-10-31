package com.galou.watchmyback.data.entity

import androidx.room.*
import com.galou.watchmyback.utils.*

/**
 * Represent the preferences of a [User]
 *
 * @property id ID of the [User]
 * @property emergencyNumber emergency number of the area
 * @property unitSystem favorite unit system
 * @property timeDisplay favorite way to display time
 * @property notificationBackSafe get notifications when a friend is back safe
 * @property notificationEmergency get notification when a friend has marked himself/herself in emergency status
 * @property notificationLate get notifications when a friend is late
 * @property notificationLocationUpdate get notification when a friend update is location
 *
 * @see Entity
 * @see User
 * @see UnitSystem
 * @see TimeDisplay
 */
@Entity(
    tableName = PREFERENCES_TABLE_NAME,
    foreignKeys = [
    ForeignKey(
        entity = User::class,
        parentColumns = [USER_TABLE_UUID],
        childColumns = [PREFERENCES_TABLE_USER_UUID],
        onDelete = ForeignKey.CASCADE
    )]
)
data class UserPreferences (
    @ColumnInfo(name = PREFERENCES_TABLE_USER_UUID) @PrimaryKey var id: String = "",
    @ColumnInfo(name = PREFERENCES_TABLE_EMERGENCY_NUMBER) var emergencyNumber: String = "",
    @ColumnInfo(name = PREFERENCES_TABLE_UNIT_SYSTEM) var unitSystem: UnitSystem = UnitSystem.METRIC,
    @ColumnInfo(name = PREFERENCES_TABLE_TIME_DISPLAY) var timeDisplay: TimeDisplay = TimeDisplay.H_24,
    @ColumnInfo(name = PREFERENCES_TABLE_NOTIFICATION_BACK_SAFE) var notificationBackSafe: Boolean = true,
    @ColumnInfo(name = PREFERENCES_TABLE_NOTIFICATION_EMERGENCY_STATUS) var notificationEmergency: Boolean = true,
    @ColumnInfo(name = PREFERENCES_TABLE_NOTIFICATION_LATE) var notificationLate: Boolean = true,
    @ColumnInfo(name = PREFERENCES_TABLE_NOTIFICATION_LOCATION_UPDATE) var notificationLocationUpdate: Boolean = true

)

/**
 * Type of Unit system available
 * Metric or imperial
 *
 * @property systemName name of the system
 *
 */
enum class UnitSystem(val systemName: String) {
    METRIC("METRIC"),
    IMPERIAL("IMPERIAL")
}

/**
 * Way to display time
 *
 * @property displayPattern pattern of display
 */
enum class TimeDisplay(val displayPattern: String){
    H_24("dd/MM/yyyy - HH:mm:ss"),
    H_12("MM/dd/yyyy - hh:mm:ssa")

}