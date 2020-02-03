package example.com.diary.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import example.com.diary.data.persistence.IDatabaseProvider
import example.com.diary.data.persistence.dao.TranslationDao
import example.com.diary.data.repositories.history.HistoryRepositoryImpl
import example.com.diary.data.repositories.translator.TranslationRepositoryImpl
import example.com.diary.domain.favourite.FavouritesInteractor
import example.com.diary.domain.favourite.FavouritesInteractorImpl
import example.com.diary.domain.history.HistoryRepository
import example.com.diary.domain.history.TranslationRepository

@Module
abstract class FavouritesFragmentModule {
    @Binds
    abstract fun provideFavouritesInteractor(favouritesInteractor: FavouritesInteractorImpl): FavouritesInteractor

    @Binds
    abstract fun provideHistoryRepository(historyRepository: HistoryRepositoryImpl): HistoryRepository

    @Binds
    abstract fun provideTranslationRepository(repository: TranslationRepositoryImpl): TranslationRepository

    @Module
    companion object {

        @Provides
        @JvmStatic
        fun provideTranslationDao(dbProvider: IDatabaseProvider): TranslationDao {
            return dbProvider.getRoomMobileDatabase().getTranslationDao()
        }
    }
}