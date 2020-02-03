package example.com.diary.di.navigation

import dagger.Module
import dagger.Provides
import example.com.diary.DiaryApplication
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router

@Module
class NavigationModule {

    @Provides
    fun provideRouter(): Router {
        return DiaryApplication.instance.getRouter()
    }

    @Provides
    fun provideNavigatorHolder(): NavigatorHolder {
        return DiaryApplication.instance.getNavigatorHolder()
    }

}