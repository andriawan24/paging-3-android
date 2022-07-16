package com.andriawan.testinglearning.data.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AppTypeConverters {

    @TypeConverter
    fun stringToList(string: String): List<String> {
        return Gson().fromJson(string, object : TypeToken<List<String>>() {}.type)
    }

    @TypeConverter
    fun listToString(list: List<String>): String {
        return Gson().toJson(list)
    }
}