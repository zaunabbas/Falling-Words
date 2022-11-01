package com.zacoding.android.fallingwords.di

import com.zacoding.android.fallingwords.domain.repository.WordsRepository
import com.zacoding.android.fallingwords.domain.use_case.FetchAllWordsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCasesModule {

    @Provides
    @ViewModelScoped
    fun provideFetchAllWordsUseCase(
        wordsRepository: WordsRepository
    ): FetchAllWordsUseCase {
        return FetchAllWordsUseCase(wordsRepository)
    }
}
