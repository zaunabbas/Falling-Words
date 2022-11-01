package com.zacoding.android.fallingwords.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zacoding.android.fallingwords.R
import com.zacoding.android.fallingwords.data.DataResource
import com.zacoding.android.fallingwords.data.model.Word
import com.zacoding.android.fallingwords.domain.repository.WordsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.reflect.Type
import javax.inject.Inject

class WordsRepositoryImpl @Inject constructor(
    private val context: Context
) : WordsRepository {

    override fun fetchAllWords() = flow {
        emit(DataResource.Loading)
        try {
            context.let { ctx ->
                val json = ctx.resources.openRawResource(R.raw.words).bufferedReader()
                    .use { it.readText() }
                val type: Type = object : TypeToken<List<Word>?>() {}.type
                val list: List<Word> = Gson().fromJson(json, type)
                emit(DataResource.Success(list))
            }
        } catch (e: Exception) {
            emit(DataResource.Error<Any>(e))
        }
    }.flowOn(Dispatchers.IO)

}