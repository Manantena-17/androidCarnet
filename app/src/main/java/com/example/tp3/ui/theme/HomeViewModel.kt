package com.example.tp3.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tp3.data.Transaction
import com.example.tp3.data.TransactionRepository
import com.example.tp3.data.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val balance: Long = 0,
    val totalIncome: Long = 0,
    val totalExpense: Long = 0,
    val transactions: List<Transaction> = emptyList()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: TransactionRepository
) : ViewModel() {

    // Le solde est calculé ICI (MVVM), pas dans l'écran
    val uiState: StateFlow<HomeUiState> = combine(
        repository.latest,
        repository.totalIncome,
        repository.totalExpense
    ) { latest, income, expense ->
        HomeUiState(
            balance = income - expense,
            totalIncome = income,
            totalExpense = expense,
            transactions = latest
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState()
    )

    init {
        seedIfEmpty() // TODO J5 : supprimer cette ligne et la fonction
    }

    // Données de test (J3) : à retirer quand le formulaire existe
    private fun seedIfEmpty() {
        viewModelScope.launch {
            if (repository.count() == 0) {
                val now = System.currentTimeMillis()
                val day = 24L * 60 * 60 * 1000
                repository.addAll(
                    listOf(
                        Transaction(type = TransactionType.ENTREE, amount = 100_000, label = "Envoi famille", date = now - 3 * day),
                        Transaction(type = TransactionType.DEPENSE, amount = 2_500, label = "Repas", date = now - 2 * day),
                        Transaction(type = TransactionType.DEPENSE, amount = 15_000, label = "Transport", date = now - day),
                        Transaction(type = TransactionType.DEPENSE, amount = 35_000, label = "Crédit téléphone", date = now)
                    )
                )
            }
        }
    }
}