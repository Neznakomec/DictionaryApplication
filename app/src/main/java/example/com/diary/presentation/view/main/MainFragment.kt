package example.com.diary.presentation.view.main

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.widget.RxTextView
import dagger.Lazy
import example.com.diary.DiaryApplication
import example.com.diary.R
import example.com.diary.utils.schedulers.SchedulerProvider
import example.com.diary.di.DaggerMainFragmentComponent
import example.com.diary.domain.models.Translation
import example.com.diary.presentation.adapter.HistoryTranslationAdapter
import example.com.diary.presentation.adapter.HistoryTranslationAdapter.ITranslationItemListener
import example.com.diary.presentation.presenter.main.MainPresenter
import example.com.diary.presentation.view.BaseFragment
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_main.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.max


class MainFragment(override val layoutRes: Int = R.layout.fragment_main) : BaseFragment(),
    MainView {
    companion object {
        private val TAG: String = MainFragment::class.java.simpleName
    }

    lateinit var historyTranslationAdapter: HistoryTranslationAdapter

    @Inject
    lateinit var schedulers: SchedulerProvider

    @Inject
    lateinit var daggerPresenter: Lazy<MainPresenter>

    @InjectPresenter
    lateinit var presenter: MainPresenter

    @ProvidePresenter
    fun providePresenter(): MainPresenter = daggerPresenter.get()

    private var transWatcher: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val component = DaggerMainFragmentComponent.builder()
            .appApi(DiaryApplication.instance.component)
            .build()
        component.inject(this)

        setHasOptionsMenu(true)

        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        transWatcher?.dispose()
        super.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLanguagePane()
        initEnterTextField()
        initAddToHistoryButton()
        initHistoryList()
    }

    private fun initLanguagePane() {
        val langList: List<String> = TranslationLanguage.languageMap.values.toList()
        setupLanguageSpinner(tv_languageFrom, langList, 0)
        setupLanguageSpinner(tv_languageTo, langList, 1)
        initChangeDirectionButton()
    }

    private fun initChangeDirectionButton() {
        ib_switch_direction.setOnClickListener {
            val selectIndexFrom: Int = tv_languageFrom.selectedItemPosition
            val selectIndexTo: Int = tv_languageTo.selectedItemPosition
            tv_languageTo.setSelection(selectIndexFrom)
            tv_languageFrom.setSelection(selectIndexTo)
        }
    }

    private fun setupLanguageSpinner(spinnerView: AppCompatSpinner, spinnerData: List<String>, selectIndex: Int = -1) {
        val adapter = ArrayAdapter<String>(context!!, android.R.layout.simple_spinner_item, spinnerData)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerView.adapter = adapter
        spinnerView.setSelection(selectIndex)
    }

    private fun initEnterTextField() {
        enter_text.setOnEditorActionListener(TextView.OnEditorActionListener { textView, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEND ->
                    translateText(textView.text.toString())
                else -> {
                    Log.w(TAG, "unknown action id $actionId")
                }
            }

            return@OnEditorActionListener true
        })
        // rx text watcher
        transWatcher = RxTextView.textChanges(enter_text)
            .debounce(500, TimeUnit.MILLISECONDS)
            .map { obj: CharSequence -> obj.toString() }
            .observeOn(schedulers.mainThread)
            .subscribe { text -> translateText(text) }
    }

    private fun initAddToHistoryButton() {
        add_to_history_button.setOnClickListener {
            presenter.addTranslationToHistory()
        }
    }

    private fun initHistoryList() {
        val layoutManager = LinearLayoutManager(context)
        recycler.layoutManager = layoutManager

        historyTranslationAdapter = HistoryTranslationAdapter()
        historyTranslationAdapter.setItemClickListener(object : ITranslationItemListener {
            override fun onItemClick(translation: Translation?) {
                translation?.let {
                    Toast.makeText(context, "${translation.id}) ${translation.textFrom}", Toast.LENGTH_LONG).show()
                    presenter.translationItemFavouritesClick(translation)
                    historyTranslationAdapter.notifyDataSetChanged()
                }
            }

            override fun onAddClick(translation: Translation?) {
                translation?.let {
                    presenter.translationItemFavouritesClick(translation)
                    historyTranslationAdapter.notifyDataSetChanged()
                }
            }

            override fun onDeleteClick(translation: Translation?) {
                historyTranslationAdapter.deleteTranslation(translation)
                translation?.let {
                    presenter.deleteTranslation(translation)
                }
            }

        })
        recycler.adapter = historyTranslationAdapter
        recycler.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    override fun setTranslations(itemViewList: List<Translation>) {
        historyTranslationAdapter.setItemViewList(itemViewList)
        historyTranslationAdapter.notifyDataSetChanged()
    }

    override fun showError(error: Throwable) {
        Toast.makeText(context, getString(R.string.translationError, error.localizedMessage), Toast.LENGTH_SHORT)
            .show()
    }

    override fun showError(@StringRes error: Int) {
        Toast.makeText(context, getString(R.string.translationError, getString(error)), Toast.LENGTH_SHORT)
            .show()
    }

    override fun showProgress() {
        progress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progress.visibility = View.GONE
    }

    override fun showTranslationResult(result: String) {
        translated_text.text = result
    }

    override fun addToHistory(translation: Translation) {
        historyTranslationAdapter.addTranslation(translation)
    }

    private fun translateText(text: String) {
        val fromPos = max(0, tv_languageFrom.selectedItemPosition)
        val languageTranslateFrom = TranslationLanguage.getLanguageCodeByPosition(fromPos)
        val toPos = max(0, tv_languageTo.selectedItemPosition)
        val languageTranslationTo = TranslationLanguage.getLanguageCodeByPosition(toPos)

        presenter.doTranslation(text, languageTranslateFrom, languageTranslationTo)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.setIconifiedByDefault(true)
        searchView.imeOptions = EditorInfo.IME_ACTION_SEARCH
        searchView.setOnQueryTextListener(historyTranslationAdapter)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favourites -> {
                presenter.onFavouritesClicked()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}