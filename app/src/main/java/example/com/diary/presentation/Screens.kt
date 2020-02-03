package example.com.diary.presentation

import androidx.fragment.app.Fragment
import example.com.diary.presentation.view.favourites.FavouritesFragment
import example.com.diary.presentation.view.main.MainFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object Screens {

    class FavouritesScreen : SupportAppScreen() {

        override fun getFragment(): Fragment {
            return FavouritesFragment()
        }
    }

    class MainScreen : SupportAppScreen() {

        override fun getFragment(): Fragment {
            return MainFragment()
        }
    }
}