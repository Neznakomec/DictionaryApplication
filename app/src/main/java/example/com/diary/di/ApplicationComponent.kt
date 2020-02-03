package example.com.diary.di

import android.app.Application
import dagger.Component
import javax.inject.Singleton

@Component(modules = [ApplicationModule::class])
@Singleton
interface ApplicationComponent : AppApi {
    fun inject(application: Application)
}