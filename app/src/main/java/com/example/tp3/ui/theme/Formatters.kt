package com.example.tp3.ui.theme


import com.example.tp3.data.TransactionType
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private val ariaryFormat = DecimalFormat(
    "#,##0",
    DecimalFormatSymbols(Locale.FRANCE).apply { groupingSeparator = '\u00A0' }
)

private val dateFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.FRANCE)

/** 12500 -> "12 500 Ar" */
fun formatAriary(amount: Long): String = "${ariaryFormat.format(amount)} Ar"

/** ENTREE 50000 -> "+ 50 000 Ar" ; DEPENSE 2500 -> "− 2 500 Ar" */
fun formatSigned(amount: Long, type: TransactionType): String {
    val sign = if (type == TransactionType.ENTREE) "+" else "−"
    return "$sign ${formatAriary(amount)}"
}

fun formatDate(epochMillis: Long): String =
    dateFormatter.format(Instant.ofEpochMilli(epochMillis).atZone(ZoneId.systemDefault()))