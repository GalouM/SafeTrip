package com.galou.watchmyback.utils

import android.util.Log
import java.util.*

/**
 * Utils class use to generate data
 *
 * @author Galou Minisini
 */

/**
 * Generate a unique and random [String] using the class [UUID]
 */
var idGenerated: String = ""
    get() {
        field = UUID.randomUUID().toString()
        return field
    }


/**
 * Generate todays date as [Date] using the class [Calendar]
 */
var todaysDate: Date = Calendar.getInstance(Locale.CANADA).time

fun displayData(message: String) = Log.e("WMT DATA", message)

fun returnSuccessOrError(localResult: Result<Void?>, remoteResult: Result<Void?>): Result<Void?>{
    return if (localResult is Result.Success && remoteResult is Result.Success) {
        Result.Success(null)
    } else {
        if(localResult is Result.Error || localResult is Result.Canceled){
            localResult
        }
        else remoteResult

    }
}
