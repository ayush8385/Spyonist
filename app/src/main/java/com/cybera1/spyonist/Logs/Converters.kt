package com.cybera1.spyonist.Logs

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromIndicatorType(indicatorType: IndicatorType): String {
        return indicatorType.name
    }

    @TypeConverter
    fun toIndicatorType(indicatorType: String): IndicatorType {
        return IndicatorType.valueOf(indicatorType)
    }
}