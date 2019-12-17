package com.galou.watchmyback.data.source

import com.galou.watchmyback.data.entity.CheckListWithItems
import com.galou.watchmyback.data.entity.TripWithData
import com.galou.watchmyback.utils.Result

/**
 * @author galou
 * 2019-12-08
 */

interface TripDataSource {

    suspend fun createTrip(trip: TripWithData, checkList: CheckListWithItems?): Result<Void?>

    suspend fun deleteActiveTrip(userId: String): Result<Void?>

    suspend fun fetchActiveTrip(userId: String): Result<TripWithData?>
}