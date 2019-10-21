package com.galou.watchmyback.utils

/**
 * Created by galou on 2019-10-20
 */

// ------ TABLES DATA -------
// tables names
const val USER_TABLE_NAME = "users"
const val TRIP_TABLE_NAME = "trips"
const val FRIEND_TABLE_NAME = "friends"
const val WATCHER_TABLE_NAME = "watchers"
const val STAGE_POINT_TABLE_NAME = "stage_points"
const val SCHEDULE_POINT_TABLE_NAME = "schedule_points"
const val LOCATION_TABLE_NAME = "locations"
const val WEATHER_DATA_TABLE_NAME = "weather_data"
const val CHECK_LIST_TABLE_NAME = "check_lists"
const val ITEM_LIST_TABLE_NAME = "items_check_list"
// tables attributes
// user
const val USER_TABLE_UUID = "user_uuid"
const val USER_TABLE_USERNAME = "username"
const val USER_TABLE_EMAIL = "email"
const val USER_TABLE_PHONE_NUMBER = "phone_number"
// trip
const val TRIP_TABLE_UUID = "trip_uuid"
const val TRIP_TABLE_TYPE = "trip_type"
const val TRIP_TABLE_FREQUENCY = "update_frequency"
const val TRIP_TABLE_STATUS = "status"
const val TRIP_TABLE_DETAILS = "details"
const val TRIP_TABLE_MAIN_LOCATION = "main location"
const val TRIP_TABLE_CHECK_LIST_UUID = "check_list_id"
const val TRIP_TABLE_USER_UUID = "user_id"
// location
const val LOCATION_TABLE_UUID = "location_uuid"
const val LOCATION_TABLE_LATITUDE = "latitude"
const val LOCATION_TABLE_LONGITUDE = "longitude"
const val LOCATION_TABLE_WEATHER_DATA = "weather_id"
// weather
const val WEATHER_DATA_TABLE_UUID = "weather_uuid"
const val WEATHER_DATA_CONDITION = "condition"
const val WEATHER_DATA_TEMPERATURE = "temperature"
// item check list
const val ITEM_TABLE_UUID = "item_uuid"
const val ITEM_TABLE_NAME = "name"
const val ITEM_TABLE_LIST_ID = "check_list_id"
const val ITEM_TABLE_CHECKED = "checked"
// check list
const val CHECK_LIST_TABLE_UUID = "default_check_list_uuid"
const val CHECK_LIST_TABLE_TRIP_TYPE = "trip_type"
const val CHECK_LIST_TABLE_USER_UUID = "user_id"
// friend
const val FRIEND_TABLE_USER_UUID = "user_id"
const val FRIEND_TABLE_USER_FRIEND_UUID = "friend_id"
// watcher
const val WATCHER_TABLE_WATCHER_UUID = "watcher_id"
const val WATCHER_TABLE_TRIP_UUID = "trip_id"
// points
const val POINT_TRIP_UUID = "point_uuid"
const val POINT_TRIP_TRIP = "trip_id"
const val POINT_TRIP_LOCATION = "location"
const val SCHEDULE_POINT_SCHEDULE_TIME = "schedule_time"
const val SCHEDULE_POINT_TYPE = "point_type"
const val STAGE_POINT_TIME = "checked_time"