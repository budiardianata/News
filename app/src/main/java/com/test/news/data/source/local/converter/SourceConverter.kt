/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.data.source.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.test.news.domain.model.Source
import com.test.news.util.fromJson
import com.test.news.util.genericType

class SourceConverter {

    @TypeConverter
    fun fromSource(source: Source): String = Gson().toJson(source, genericType<Source>())

    @TypeConverter
    fun toSource(name: String): Source = Gson().fromJson(name)
}
