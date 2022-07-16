package com.andriawan.testinglearning.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.andriawan.testinglearning.data.local.converters.AppTypeConverters
import com.andriawan.testinglearning.data.local.dao.CharacterDAO
import com.andriawan.testinglearning.data.local.dao.CharacterRemoteKeyDAO
import com.andriawan.testinglearning.data.models.CharacterDTO
import com.andriawan.testinglearning.data.models.paging.CharacterRemoteKey
import com.andriawan.testinglearning.utils.Constants.DATABASE_NAME

@Database(entities = [CharacterDTO::class, CharacterRemoteKey::class], version = 1, exportSchema = false)
@TypeConverters(AppTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDAO
    abstract fun characterRemoteKeyDao(): CharacterRemoteKeyDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun newInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE = Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()
                return INSTANCE!!
            }
    }
}