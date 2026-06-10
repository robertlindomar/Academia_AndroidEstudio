package com.example.appacademia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appacademia.data.repository.AlunoRepositorio
import com.example.appacademia.data.repository.PagamentoRepositorio
import com.example.appacademia.data.repository.PlanoRepositorio
import com.example.appacademia.data.repository.TreinoRepositorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DashboardUiEstado(
    val totalAlunos: Int = 0,
    val totalPlanos: Int = 0,
    val totalTreinos: Int = 0,
    val totalPagamentosPendentes: Int = 0
)

class DashboardViewModel(
    alunoRepositorio: AlunoRepositorio,
    planoRepositorio: PlanoRepositorio,
    treinoRepositorio: TreinoRepositorio,
    pagamentoRepositorio: PagamentoRepositorio
) : ViewModel() {

    private val _estado = MutableStateFlow(DashboardUiEstado())
    val estado: StateFlow<DashboardUiEstado> = _estado.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                alunoRepositorio.contarTodos(),
                planoRepositorio.contarTodos(),
                treinoRepositorio.contarTodos(),
                pagamentoRepositorio.contarPendentes()
            ) { alunos, planos, treinos, pendentes ->
                DashboardUiEstado(alunos, planos, treinos, pendentes)
            }.collect { resumo ->
                _estado.update { resumo }
            }
        }
    }
}
