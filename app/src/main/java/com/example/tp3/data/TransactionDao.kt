package com.example.tp3.data


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert
    suspend fun insert(transaction: Transaction)

    @Insert
    suspend fun insertAll(transactions: List<Transaction>)

    @Query("SELECT COUNT(*) FROM transactions")
    suspend fun count(): Int

    // Les 20 plus récentes, la plus récente en premier
    @Query("SELECT * FROM transactions ORDER BY date DESC LIMIT 20")
    fun observeLatest(): Flow<List<Transaction>>

    // Total par type, 0 s'il n'y a aucune transaction
    @Query("SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = :type")
    fun observeTotal(type: TransactionType): Flow<Long>
}