package com.inar.kickercompose.data.models.adapters

import com.google.gson.*
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeSerializer : JsonSerializer<LocalDateTime> {
    override fun serialize(
        src: LocalDateTime,
        typeOfSrc: Type,
        context: JsonSerializationContext,
    ): JsonElement {
        val s = src.format(DateTimeFormatter.ISO_DATE_TIME)
        return JsonPrimitive(s)
    }
}

class LocalDateTimeDeserializer : JsonDeserializer<LocalDateTime> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext,
    ): LocalDateTime {
        return LocalDateTime.parse(json.asString);
    }

}