package com.example.android.politicalpreparedness.data.network.jsonadapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import timber.log.Timber
import java.text.DateFormat
import java.text.ParseException
import java.util.*

class DateAdapter {
    @FromJson
    fun dateFromJson(electionDay: String): Date {
        val myDate = DateFormat.getDateInstance()
        try {
            myDate.parse(electionDay)
        } catch (e: ParseException) {
            Timber.e("Error parsing date")
        } finally {
            return Date()
        }
    }

    @ToJson
    fun dateToJson(date: Date): String {
        return date.toString()
    }
}