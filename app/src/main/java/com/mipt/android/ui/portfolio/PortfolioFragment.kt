package com.mipt.android.ui.portfolio

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.mipt.android.R
import com.mipt.android.data.api.responses.portfolio.PortfolioResponse
import com.mipt.android.databinding.PortfolioFragmentBinding
import com.mipt.android.tools.navigate
import com.mipt.android.ui.auth.AuthFragment
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat


class HistoryItemAdapter(private val context: Context,
                         private val dataSource: List<PortfolioResponse.PositionItem>) : BaseAdapter() {

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
        balanceTextView.text = recipe.balance.toBigDecimal().toPlainString()
        lotsTextview.text = recipe.lots

        if (recipe.instrumentType == "Currency") {
            photo.setImageResource(R.drawable.icon_money)
        } else if (recipe.instrumentType == "Stonks") {
            photo.setImageResource(R.drawable.stonks_icon)
        }

        rowView.setOnClickListener {
            val figi = recipe.figi;
            // переход по акции
        }
        return rowView
    }
}

@AndroidEntryPoint
class PortfolioFragment : Fragment(R.layout.portfolio_fragment) {
    private val viewBinding by viewBinding(PortfolioFragmentBinding::bind)
    private val viewModel by viewModels<PortfolioViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewBinding) {
            profileButton.setOnClickListener {
                showAuth()
            }
            detailsButton.setOnClickListener {
                showDetails()
            }

            viewModel.toast.observe(viewLifecycleOwner, { error ->
                val toast = Toast.makeText(context, error, Toast.LENGTH_LONG)
                toast.show()
            });

            viewModel.result.observe(viewLifecycleOwner, { array ->
                val listView: ListView = listPortfoli
                val adapter = activity?.let { HistoryItemAdapter(it, array) }
                listView.adapter = adapter
            })

        }
    }

    private fun showAuth() {
        parentFragmentManager.navigate(AuthFragment(), true)
    }

    private fun showDetails(){
        parentFragmentManager.navigate(DetailsFragment(), true)
    }
}
