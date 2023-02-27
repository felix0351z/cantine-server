package de.juliando.app.repository

import de.juliando.app.models.objects.*

interface PaymentRepository {

    //Credit
    @Throws(Exception::class)
    suspend fun getCredit(): Float

    //Order
    @Throws(Exception::class)
    suspend fun getOrders(): List<Content.Order>
    @Throws(Exception::class)
    suspend fun createOrderRequest(request: CreateOrderRequest): Content.Order
    @Throws(Exception::class)
    suspend fun deleteOrder(id: String): Content.Order
    @Throws(Exception::class)
    suspend fun verifyOrder(request: VerifyOrderRequest)

    //Purchase
    @Throws(Exception::class)
    suspend fun getPurchases(): List<Auth.Payment>
    @Throws(Exception::class)
    suspend fun deletePurchases()

    //User
    @Throws(Exception::class)
    suspend fun addUserCredit(request: AddCreditRequest)

}