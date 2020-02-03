package example.com.diary.di

import dagger.Component
import example.com.diary.di.navigation.NavigationModule
import example.com.diary.presentation.view.favourites.FavouritesFragment

@Component(modules = [FavouritesFragmentModule::class, NavigationModule::class], dependencies = [AppApi::class])
interface FavouritesFragmentComponent {
    fun inject(fragment: FavouritesFragment)
}