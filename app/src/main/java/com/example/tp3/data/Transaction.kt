package com.example.tp3.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TransactionType { ENTREE, DEPENSE }

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: TransactionType,
    val amount: Long,   // en Ariary, sans décimales
    val label: String,
    val date: Long      // epoch en millisecondes
)