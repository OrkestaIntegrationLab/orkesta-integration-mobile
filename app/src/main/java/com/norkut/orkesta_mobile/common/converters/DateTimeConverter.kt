package com.norkut.orkesta_mobile.common.converters

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class DateTimeConverter : JsonDeserializer<LocalDateTime>, JsonSerializer<LocalDateTime> {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDateTime {
        return LocalDateTime.parse(
            json?.asString?.substringBefore(".") ?: "1900-01-01'T'00:00:00",
            timeFormatter
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun serialize(
        src: LocalDateTime?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src?.format(timeFormatter))
    }

    private val timeFormatter : DateTimeFormatter?
        @RequiresApi(Build.VERSION_CODES.O)
        get() = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            .withZone(ZoneId.of("UTC"))
}