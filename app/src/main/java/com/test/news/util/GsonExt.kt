/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Reader
import java.lang.reflect.Type

inline fun <reified T> genericType(): Type = object : TypeToken<T>() {}.type

inline fun <reified T> Gson.fromJson(json: String): T = fromJson(json, genericType<T>())

inline fun <reified T> Gson.fromJson(json: Reader?): T = fromJson(json, genericType<T>())
