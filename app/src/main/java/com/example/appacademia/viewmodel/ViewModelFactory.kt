package com.example.appacademia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.appacademia.AcademiaApplication
import com.example.appacademia.data.repository.AlunoRepositorio
import com.example.appacademia.data.repository.PagamentoRepositorio
import com.example.appacademia.data.repository.PlanoRepositorio
import com.example.appacademia.data.repository.TreinoRepositorio

class ViewModelFactory(
    private val alunoRepositorio: AlunoRepositorio,
    private val planoRepositorio: PlanoRepositorio,
    private val treinoRepositorio: TreinoRepositorio,
    private val pagamentoRepositorio: PagamentoRepositorio
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DashboardViewModel::class.java) ->
                DashboardViewModel(alunoRepositorio, planoRepositorio, treinoRepositorio, pagamentoRepositorio) as T
            modelClass.isAssignableFrom(AlunoViewModel::class.java) ->
                AlunoViewModel(alunoRepositorio) as T
            modelClass.isAssignableFrom(PlanoViewModel::class.java) ->
                PlanoViewModel(planoRepositorio) as T
            modelClass.isAssignableFrom(TreinoViewModel::class.java) ->
                TreinoViewModel(treinoRepositorio, alunoRepositorio) as T
            modelClass.isAssignableFrom(PagamentoViewModel::class.java) ->
                PagamentoViewModel(pagamentoRepositorio, alunoRepositorio) as T
            else -> throw IllegalArgumentException("ViewModel desconhecido: ${modelClass.name}")
        }
    }

    companion object {
        fun criar(application: AcademiaApplication): ViewModelFactory {
            return ViewModelFactory(
                application.alunoRepositorio,
                application.planoRepositorio,
                application.treinoRepositorio,
                application.pagamentoRepositorio
            )
        }
    }
}
