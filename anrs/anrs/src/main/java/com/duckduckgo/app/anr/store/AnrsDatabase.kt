package com.duckduckgo.app.anr.store

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types.newParameterizedType

@Database(
    exportSchema = true,
    version = 1,
    entities = [AnrEntity::class]
)
@TypeConverters(AnrTypeConverter::class)
abstract class AnrsDatabase : RoomDatabase() {
    abstract fun arnDao(): AnrDao
}

object AnrTypeConverter {

    private val stringListType = newParameterizedType(List::class.java, String::class.java)
    private val stringListAdapter: JsonAdapter<List<String>> = Moshi.Builder().build().adapter(
        stringListType
    )

    @TypeConverter
    @JvmStatic
    fun toStringList(value: String): List<String> {
        return stringListAdapter.fromJson(value)!!
    }

    @TypeConverter
    @JvmStatic
    fun fromStringList(value: List<String>): String {
        return stringListAdapter.toJson(value)
    }
}
