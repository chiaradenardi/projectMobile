package com.example.projectmobile.utilis

import androidx.room.TypeConverter
import com.example.projectmobile.ui.components.PieSlice
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromPieSliceListToJson(pieSlices: List<PieSlice>?): String? {
        return Gson().toJson(pieSlices)
    }

    @TypeConverter
    fun fromJsonToPieSliceList(json: String?): List<PieSlice>? {
        val type = object : TypeToken<List<PieSlice>>() {}.type
        return Gson().fromJson(json, type)
    }
}


