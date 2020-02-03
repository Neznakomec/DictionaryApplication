package example.com.diary.di

import dagger.Component
import example.com.diary.di.navigation.NavigationModule
import example.com.diary.presentation.view.main.MainFragment

@Component(modules = [MainFragmentModule::class, NavigationModule::class], dependencies = [AppApi::class])
interface MainFragmentComponent {
    fun inject(fragment: MainFragment)
}