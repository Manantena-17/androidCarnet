package com.example.tp3.ui.theme


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.carnetdepenses.data.Transaction
import com.example.carnetdepenses.data.TransactionType

private val Green = Color(0xFF2E7D32)
private val Red = Color(0xFFC62828)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Carnet de dépenses") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* TODO J5 : ouvrir le formulaire */ }) {
                Icon(Icons.Default.Add, contentDescription = "Nouvelle transaction")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Dashboard(
                    balance = state.balance,
                    income = state.totalIncome,
                    expense = state.totalExpense
                )
            }
            item {
                Text(
                    "Dernières transactions",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            if (state.transactions.isEmpty()) {
                item {
                    Text(
                        "Aucune transaction pour l'instant",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp)
                    )
                }
            } else {
                items(state.transactions, key = { it.id }) { TransactionRow(it) }
            }
        }
    }
}

@Composable
private fun Dashboard(balance: Long, income: Long, expense: Long) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SummaryCard(
            title = "Solde",
            value = formatAriary(balance),
            valueColor = if (balance < 0) Red else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth()
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryCard("Entrées", formatAriary(income), Green, Modifier.weight(1f))
            SummaryCard("Dépenses", formatAriary(expense), Red, Modifier.weight(1f))
        }
    }
}

@Composable
private fun SummaryCard(
    title: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.labelLarge)
            Text(
                value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = valueColor
            )
        }
    }
}

@Composable
private fun TransactionRow(transaction: Transaction) {
    val isIncome = transaction.type == TransactionType.ENTREE
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(Modifier.weight(1f)) {
                Text(transaction.label, style = MaterialTheme.typography.bodyLarge)
                Text(formatDate(transaction.date), style = MaterialTheme.typography.bodySmall)
            }
            Text(
                formatSigned(transaction.amount, transaction.type),
                color = if (isIncome) Green else Red,
                fontWeight = FontWeight.SemiBold
            )
        }
        HorizontalDivider()
    }
}