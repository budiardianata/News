/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.data.mapper

abstract class Mapper<I, O> {
    abstract fun map(input: I): O
    fun listMap(input: List<I>): List<O> = input.map(::map)
}
