package com.zacoding.android.fallingwords.data.model

import com.google.gson.annotations.SerializedName

data class Word(
    @SerializedName("text_eng")
    val textEnglish: String,
    @SerializedName("text_spa")
    val textSpanish: String
)
