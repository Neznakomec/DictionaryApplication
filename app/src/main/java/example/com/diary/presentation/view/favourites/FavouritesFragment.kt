package example.com.diary.presentation.view.favourites

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.Lazy
import example.com.diary.DiaryApplication
import example.com.diary.R
import example.com.diary.di.DaggerFavouritesFragmentComponent
import example.com.diary.domain.models.Translation
import example.com.diary.presentation.adapter.HistoryTranslationAdapter
import example.com.diary.presentation.presenter.favourites.FavouritesPresenter
import example.com.diary.presentation.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_main.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class FavouritesFragment(override val layoutRes: Int = R.layout.fragment_favourites) : BaseFragment(),
    FavouritesView {

    @Inject
    lateinit var daggerPresenter: Lazy<FavouritesPresenter>

    @InjectPresenter
    lateinit var presenter: FavouritesPresenter

    @ProvidePresenter
    fun providePresenter(): FavouritesPresenter = daggerPresenter.get()

    lateinit var historyTranslationAdapter: HistoryTranslationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        val component = DaggerFavouritesFragmentComponent.builder()
            .appApi(DiaryApplication.instance.component)
            .build()

        component.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHistoryList()
    }

    private fun initHistoryList() {
        val layoutManager = LinearLayoutManager(context)
        recycler.layoutManager = layoutManager

        historyTranslationAdapter = HistoryTranslationAdapter()
            .setCustomLayoutId(R.layout.item_translation_favourites)
        historyTranslationAdapter.setItemClickListener(object :
            HistoryTranslationAdapter.ITranslationItemListener {
            override fun onItemClick(translation: Translation?) {
                translation?.let {
                    Toast.makeText(context, "${translation.id}) ${translation.textFrom}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onAddClick(translation: Translation?) {
                translation?.let {
                    Toast.makeText(context, "${translation.id}) ${translation.textFrom}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onDeleteClick(translation: Translation?) {
                historyTranslationAdapter.deleteTranslation(translation)
                translation?.let {
                    translation.isFavourite = false
                    presenter.writeTranslationToHistory(translation)
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

    override fun showProgress() {
        progress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progress.visibility = View.GONE
    }

    override fun onBackPressed(): Boolean {
        presenter.onBackPressed()
        return true
    }
}