package com.example.appacademia

import android.app.Application
import com.example.appacademia.data.database.AppDatabase
import com.example.appacademia.data.repository.AlunoRepositorio
import com.example.appacademia.data.repository.PagamentoRepositorio
import com.example.appacademia.data.repository.PlanoRepositorio
import com.example.appacademia.data.repository.TreinoRepositorio

class AcademiaApplication : Application() {

    val banco by lazy { AppDatabase.obterInstancia(this) }

    val alunoRepositorio by lazy {
        AlunoRepositorio(banco.alunoDao(), banco.treinoDao(), banco.pagamentoDao())
    }
    val planoRepositorio by lazy { PlanoRepositorio(banco.planoDao()) }
    val treinoRepositorio by lazy { TreinoRepositorio(banco.treinoDao()) }
    val pagamentoRepositorio by lazy { PagamentoRepositorio(banco.pagamentoDao()) }
}
