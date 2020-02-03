package example.com.diary.di.mainmenu

import android.content.Context
import dagger.Module
import dagger.Provides
import example.com.diary.DiaryApplication
import example.com.diary.di.FeatureScope

@Module
abstract class MainMenuModule {
    @Module
    companion object {
        @Provides
        @JvmStatic
        @FeatureScope
        fun provideContext(): Context {
            return DiaryApplication.instance.applicationContext
        }
    }
}