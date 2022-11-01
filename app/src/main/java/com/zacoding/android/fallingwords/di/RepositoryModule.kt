package com.zacoding.android.fallingwords.di

import android.content.Context
import com.zacoding.android.fallingwords.data.repository.WordsRepositoryImpl
import com.zacoding.android.fallingwords.domain.repository.WordsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

  @Provides
  @ViewModelScoped
  fun provideWordsRepository(
    @ApplicationContext context: Context
  ): WordsRepository {
    return WordsRepositoryImpl(context)
  }
}
