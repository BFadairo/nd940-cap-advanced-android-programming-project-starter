package com.example.android.politicalpreparedness.data.network.jsonadapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import timber.log.Timber
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateAdapter {
    @FromJson
    fun dateFromJson(electionDay: String): Date {
        val myFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return try {
            val myDate = myFormatter.parse(electionDay)
            myDate ?: Date()
        } catch (e: ParseException) {
            Timber.e("Error parsing date")
            Date()
        }
    }

    @ToJson
    fun dateToJson(date: Date): String {
        return date.toString()
    }
}