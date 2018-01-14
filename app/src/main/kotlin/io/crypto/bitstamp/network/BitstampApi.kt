package io.crypto.bitstamp.network

import io.crypto.bitstamp.model.OrderBook
import io.crypto.bitstamp.model.Ticker
import io.crypto.bitstamp.model.TradingPair
import io.crypto.bitstamp.model.Transaction
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BitstampApi {
	@GET("/api/v2/order_book/{currency_pair}")
	fun getOrderBook(@Path("currency_pair") currencyPair: String): Call<OrderBook>

	@GET("/api/v2/ticker/{currency_pair}")
	fun getTicker(@Path("currency_pair") currencyPair: String): Call<Ticker>

	@GET("/api/v2/trading-pairs-info")
	fun getTradingPairs(): Call<List<TradingPair>>

	@GET("/api/v2/transactions/{currency_pair}")
	fun getTransactions(@Path("currency_pair") currencyPair: String): Call<List<Transaction>>

	@POST("/api/v2/user_transactions")
	fun getUserTransactions(): Call<ResponseBody>
}