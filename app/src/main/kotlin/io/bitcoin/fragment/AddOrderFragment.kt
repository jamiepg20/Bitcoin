package io.bitcoin.fragment

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.content.LocalBroadcastManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import io.bitcoin.BuildConfig
import io.bitcoin.Config
import io.bitcoin.R
import io.bitcoin.extension.saveOrder
import io.bitcoin.extension.toCurrencyPair
import io.bitcoin.model.CurrencyPair
import io.bitcoin.model.Order
import kotlinx.android.synthetic.main.fragment_add_order.add
import kotlinx.android.synthetic.main.fragment_add_order.currency_pair
import kotlinx.android.synthetic.main.fragment_add_order.fees
import kotlinx.android.synthetic.main.fragment_add_order.order_id
import kotlinx.android.synthetic.main.fragment_add_order.quantity
import kotlinx.android.synthetic.main.fragment_add_order.unit_price

class AddOrderFragment : BottomSheetDialogFragment(), View.OnClickListener {
	override fun onClick(view: View) {
		val order = this.createOrderFromUserInputs() ?: return

		this.saveOrder(order)
		this.notifyOrderAdded()
		this.dismiss()
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
			= inflater.inflate(R.layout.fragment_add_order, container, false)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		val currencyPairs = BuildConfig.CURRENCY_PAIRS.map { it.toCurrencyPair() }

		this.currency_pair.adapter = ArrayAdapter<CurrencyPair>(this.context, android.R.layout.simple_list_item_1, currencyPairs)

		this.add.setOnClickListener(this)
	}

	private fun createOrderFromUserInputs(): Order? {
		val currencyPair = this.currency_pair.selectedItem as CurrencyPair? ?: return null
		val fees = this.fees.text?.toString()?.toFloatOrNull() ?: this.getString(R.string.default_fees).toFloatOrNull() ?: return null
		val id = this.order_id.text?.toString()?.toIntOrNull() ?: return null
		val quantity = this.quantity?.text?.toString()?.toDoubleOrNull() ?: return null
		val unitPrice = this.unit_price?.text?.toString()?.toDoubleOrNull() ?: return null

		return Order(currencyPair, fees / 100f, id, quantity, unitPrice)
	}

	private fun notifyOrderAdded() {
		this.context?.let {
			LocalBroadcastManager.getInstance(it)
					.sendBroadcast(Intent(Config.ACTION_ORDER_ADDED))
		}
	}

	private fun saveOrder(order: Order) {
		PreferenceManager.getDefaultSharedPreferences(this.context).saveOrder(order)
	}

	companion object {
		fun newInstance() = AddOrderFragment()
	}
}