package example.com.diary.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import example.com.diary.data.repositories.history.HistoryRepositoryImpl
import example.com.diary.data.ITranslationService
import example.com.diary.data.persistence.IDatabaseProvider
import example.com.diary.data.persistence.dao.TranslationDao
import example.com.diary.data.repositories.translator.TranslationRepositoryImpl
import example.com.diary.data.yandextranslator.retrofit.YandexTranslationServiceRetrofit
import example.com.diary.domain.history.HistoryInteractor
import example.com.diary.domain.history.HistoryInteractorImpl
import example.com.diary.domain.history.HistoryRepository
import example.com.diary.domain.history.TranslationRepository

@Module
abstract class MainFragmentModule {
    @Binds
    abstract fun provideHistoryInteractor(historyInteractor: HistoryInteractorImpl): HistoryInteractor

    @Binds
    abstract fun provideHistoryRepository(historyRepository: HistoryRepositoryImpl): HistoryRepository

    @Binds
    abstract fun provideTranslationRepository(repository: TranslationRepositoryImpl): TranslationRepository

    @Module
    companion object {
        @Provides
        @JvmStatic
        fun provideTranslationService(): ITranslationService {
            return YandexTranslationServiceRetrofit.create()
        }

        @Provides
        @JvmStatic
        fun provideTranslationDao(dbProvider: IDatabaseProvider): TranslationDao {
            return dbProvider.getRoomMobileDatabase().getTranslationDao()
        }
    }
}