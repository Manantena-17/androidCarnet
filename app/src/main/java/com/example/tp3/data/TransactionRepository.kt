package com.example.tp3.data


import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepository @Inject constructor(
    private val dao: TransactionDao
) {
    val latest: Flow<List<Transaction>> = dao.observeLatest()
    val totalIncome: Flow<Long> = dao.observeTotal(TransactionType.ENTREE)
    val totalExpense: Flow<Long> = dao.observeTotal(TransactionType.DEPENSE)

    suspend fun add(transaction: Transaction) = dao.insert(transaction)
    suspend fun addAll(transactions: List<Transaction>) = dao.insertAll(transactions)
    suspend fun count(): Int = dao.count()
}