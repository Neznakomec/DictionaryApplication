package example.com.diary

import android.app.Application
import example.com.diary.di.ApplicationComponent
import example.com.diary.di.ApplicationModule
import example.com.diary.di.DaggerApplicationComponent
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router


class DiaryApplication : Application() {

    lateinit var component: ApplicationComponent
        private set

    private lateinit var cicerone: Cicerone<Router>

    override fun onCreate() {
        super.onCreate()

        instance = this
        setupDaggerAppComponent()
        initCicerone()
    }

    private fun setupDaggerAppComponent() {
        component = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()

        component.inject(this)
    }

    private fun initCicerone() {
        cicerone = Cicerone.create()
    }

    fun getNavigatorHolder(): NavigatorHolder {
        return cicerone.navigatorHolder
    }

    fun getRouter(): Router {
        return cicerone.router
    }

    companion object {
        lateinit var instance: DiaryApplication private set
    }
}