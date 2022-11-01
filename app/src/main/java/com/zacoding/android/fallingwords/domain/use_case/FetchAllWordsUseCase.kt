package com.zacoding.android.fallingwords.domain.use_case

import com.zacoding.android.fallingwords.data.DataResource
import com.zacoding.android.fallingwords.data.model.Word
import com.zacoding.android.fallingwords.domain.repository.WordsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class FetchAllWordsUseCase(
    private val wordsRepository: WordsRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
) : FlowUseCase<Unit, DataResource<List<Word>>>(dispatcher) {

    override fun execute(parameters: Unit): Flow<DataResource<List<Word>>> {
        return wordsRepository.fetchAllWords()
    }
}