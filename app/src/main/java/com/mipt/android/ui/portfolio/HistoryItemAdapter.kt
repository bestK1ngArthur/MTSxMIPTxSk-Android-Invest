package com.mipt.android.ui.portfolio

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.mipt.android.R
import com.mipt.android.data.TinkoffRepository
import com.mipt.android.data.api.responses.portfolio.PortfolioResponse

class HistoryItemAdapter(
    private val context: Context,
    private val dataSource: List<PortfolioResponse.PositionItem>,
    private val onItemClicked: (String) -> Unit
) : BaseAdapter() {

    private val objectList: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder", "SimpleDateFormat")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView = objectList.inflate(R.layout.portfolio_item_fragment, parent, false)

        val titleTextView = rowView.findViewById<TextView>(R.id.title)
        val balanceTextView = rowView.findViewById<TextView>(R.id.balance)
        val lotsTextview = rowView.findViewById<TextView>(R.id.lots)
        val photo = rowView.findViewById<ImageView>(R.id.imageView)
        val recipe = getItem(position) as PortfolioResponse.PositionItem

        titleTextView.text = recipe.name
        balanceTextView.text = recipe.ticker //recipe.price.toString(); /* recipe.balance.toBigDecimal().toPlainString() */
        lotsTextview.text = recipe.lots

        if (recipe.instrumentType == "Currency") {
            photo.setImageResource(R.drawable.icon_money)
        } else {
            photo.setImageResource(R.drawable.stonks_icon)
        }

        rowView.setOnClickListener {
            onItemClicked(recipe.figi)
        }

        return rowView
    }
}