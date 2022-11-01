package com.zacoding.android.fallingwords.domain.repository

import com.zacoding.android.fallingwords.data.DataResource
import com.zacoding.android.fallingwords.data.model.Word
import kotlinx.coroutines.flow.Flow

interface WordsRepository {
    fun fetchAllWords(): Flow<DataResource<List<Word>>>
}