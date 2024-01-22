package com.oreomrone.locallens.ui.utils

import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun jsonTimestampToDateTime(jsonTimestamp: String): String {
    return try {
        // Parse the JSON timestamp to OffsetDateTime
        val offsetDateTime = OffsetDateTime.parse(jsonTimestamp, DateTimeFormatter.ISO_OFFSET_DATE_TIME)

        // Convert to desired time zone (e.g., from UTC to the system default time zone)
        val convertedDateTime = offsetDateTime.atZoneSameInstant(ZoneId.systemDefault()).toOffsetDateTime()

        // Define the desired output format with AM/PM based on locale
        val outputFormat = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a", Locale.getDefault())

        // Format the OffsetDateTime to the desired format
        return convertedDateTime.format(outputFormat)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}