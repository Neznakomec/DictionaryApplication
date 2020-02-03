package example.com.diary.di

import com.vanniktech.rxpermission.RealRxPermission
import com.vanniktech.rxpermission.RxPermission
import dagger.Module
import dagger.Provides
import example.com.diary.DiaryApplication
import example.com.diary.data.persistence.DatabaseFactory
import example.com.diary.data.persistence.DatabaseProvider
import example.com.diary.data.persistence.IDatabaseProvider
import example.com.diary.utils.schedulers.DefaultSchedulers
import example.com.diary.utils.schedulers.SchedulerProvider
import javax.inject.Singleton

@Module
class ApplicationModule(private var application: DiaryApplication) {

    @Provides
    @Singleton
    fun provideApplication(): DiaryApplication {
        return application
    }

    @Provides
    @Singleton
    fun provideDataBaseProvider(): IDatabaseProvider {
        return DatabaseProvider(DatabaseFactory(application))
    }

    @Provides
    @Singleton
    fun provideRxPermission(): RxPermission {
        return RealRxPermission.getInstance(application)
    }

    @Provides
    @Singleton
    fun provideRxSchedulers(): SchedulerProvider {
        return DefaultSchedulers()
    }
}