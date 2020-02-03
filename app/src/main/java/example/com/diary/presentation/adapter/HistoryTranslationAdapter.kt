package example.com.diary.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import example.com.diary.R
import example.com.diary.domain.models.Translation
import example.com.diary.presentation.adapter.HistoryTranslationAdapter.TranslationViewHolder
import java.util.*

class HistoryTranslationAdapter : RecyclerView.Adapter<TranslationViewHolder>(),
    SearchView.OnQueryTextListener, Filterable {
    private val DEFAULT_LOCALE: Locale = Locale.ROOT
    private var translationHistory: MutableList<Translation> = mutableListOf<Translation>()
    private var translationHistoryAll: MutableList<Translation> = mutableListOf<Translation>()
    private var itemListener: ITranslationItemListener? = null

    @LayoutRes
    private var layoutId = R.layout.item_translation

    fun setItemClickListener(iTranslationItemListener: ITranslationItemListener?) {
        this.itemListener = iTranslationItemListener
        notifyDataSetChanged()
    }

    fun setCustomLayoutId(@LayoutRes customLayoutId: Int): HistoryTranslationAdapter {
        layoutId = customLayoutId
        notifyDataSetChanged()
        return this
    }

    fun addTranslation(translation: Translation) {
        translationHistory.add(0, translation)
        translationHistoryAll.add(0, translation)
        notifyItemInserted(0)
    }

    fun deleteTranslation(translation: Translation?) {
        val index: Int = translationHistory.indexOf(translation)
        if (index >= 0) {
            translationHistory.removeAt(index)
            translationHistoryAll.remove(translation)
            notifyItemRemoved(index)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TranslationViewHolder {
        return TranslationViewHolder(
            LayoutInflater.from(parent.context).inflate(
                layoutId,
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: TranslationViewHolder,
        position: Int
    ) {
        val item: Translation = translationHistory[position]
        holder.title.text = Formatter.format(item)
        holder.itemView.setOnClickListener {
            itemListener?.onItemClick(item)
        }

        holder.delete.visibility = View.VISIBLE

        holder.delete.setOnClickListener {
            itemListener?.onDeleteClick(item)
        }

        holder.add.setOnClickListener {
            itemListener?.onAddClick(item)
        }

        val res = holder.itemView.resources
        val ctx = holder.itemView.context
        holder.add.setImageDrawable(if (item.isFavourite)
            res.getDrawable(R.drawable.ic_favourite_added, ctx.theme) else
            res.getDrawable(R.drawable.ic_favourite_to_add, ctx.theme))
    }

    override fun getItemCount(): Int {
        return translationHistory.size
    }

    fun setItemViewList(itemViewList: List<Translation>) {
        translationHistory = itemViewList.toMutableList()
        translationHistoryAll = itemViewList.toMutableList()
    }

    interface ITranslationItemListener {
        fun onItemClick(translation: Translation?)
        fun onAddClick(translation: Translation?)
        fun onDeleteClick(translation: Translation?)
    }

    class TranslationViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.title)
        var add: ImageView = itemView.findViewById(R.id.add_favourites)
        var delete: ImageView = itemView.findViewById(R.id.delete)
    }

    // Search View
    override fun onQueryTextSubmit(query: String?): Boolean {
        filter.filter(query)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        filter.filter(newText)
        return true
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(
                constraint: CharSequence,
                results: FilterResults
            ) {
                translationHistory = (results.values as List<Translation>).toMutableList()
                notifyDataSetChanged()
            }

            override fun performFiltering(constraint: CharSequence): FilterResults {
                val results = FilterResults()
                results.values = if (constraint.isEmpty()) {
                    translationHistoryAll
                } else {
                    getFilteredResults(constraint.toString().toLowerCase(DEFAULT_LOCALE))
                }
                return results
            }
        }
    }

    fun getFilteredResults(constraint: String): List<Translation> {
        return translationHistory.filter { Formatter.getFullText(it).toLowerCase(DEFAULT_LOCALE).contains(constraint) }
    }

    private object Formatter {
        fun format(item: Translation): String {
            return "(${item.langFrom})${item.textFrom} -> (${item.langTo})${item.textTo}"
        }

        fun getFullText(item: Translation): String {
            return format(item)
        }
    }


}