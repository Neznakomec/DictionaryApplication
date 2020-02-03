package example.com.diary

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.vanniktech.rxpermission.RxPermission
import example.com.diary.di.mainmenu.DaggerMainMenuComponent
import example.com.diary.presentation.Screens
import example.com.diary.presentation.presenter.main.MainActivityPresenter
import example.com.diary.presentation.view.BaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Replace
import javax.inject.Inject

class MainActivity : MvpAppCompatActivity(R.layout.activity_main) {

    @Inject
    lateinit var rxPermission: RxPermission
    @Inject
    lateinit var presenter: MainActivityPresenter
    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    private val navigator: Navigator = SupportAppNavigator(this, R.id.container)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)

        val component = DaggerMainMenuComponent.builder()
            .appApi(DiaryApplication.instance.component)
            .build()
        component.inject(this)

        if (savedInstanceState == null) {
            navigator.applyCommands(
                arrayOf<Command>(
                    Replace(Screens.MainScreen())
                )
            )
        }
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }

    override fun onBackPressed() {
        val f = supportFragmentManager.findFragmentById(R.id.container) as BaseFragment
            if (f.onBackPressed()) {
                return
            }

        super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        menu.findItem(R.id.action_unload).isVisible = BuildConfig.DEBUG
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_unload -> {
                rxPermission.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .map { it.name() }
                    .subscribe( { permissionName: String ->
                        if (rxPermission.isGranted(permissionName))
                            presenter.copyDatabase()
                    },
                        { throwable -> Log.d("Permission", "a problem occured on asking permission", throwable)} )
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
