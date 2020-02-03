package example.com.diary.di.mainmenu

import dagger.Component
import example.com.diary.MainActivity
import example.com.diary.di.AppApi
import example.com.diary.di.FeatureScope
import example.com.diary.di.navigation.NavigationModule

@FeatureScope
@Component(modules = [MainMenuModule::class, NavigationModule::class], dependencies = [AppApi::class])
interface MainMenuComponent {
    fun inject(activity: MainActivity)
}