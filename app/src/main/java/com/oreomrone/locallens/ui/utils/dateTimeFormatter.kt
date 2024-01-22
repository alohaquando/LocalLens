package com.oreomrone.locallens.ui.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

fun JsonTimestampToDateTime(jsonTimestamp: String): String {
    // Parse the JSON timestamp
    val offsetDateTime = OffsetDateTime.parse(jsonTimestamp, DateTimeFormatter.ISO_OFFSET_DATE_TIME)

    // Define the desired output format
    val outputFormat = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm a", Locale.getDefault())

    // Format the OffsetDateTime to the desired format
    return offsetDateTime.format(outputFormat)
}