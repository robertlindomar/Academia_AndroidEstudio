package com.example.appacademia.data.repository

import com.example.appacademia.data.dao.TreinoDao
import com.example.appacademia.data.entity.Treino
import kotlinx.coroutines.flow.Flow

class TreinoRepositorio(private val treinoDao: TreinoDao) {

    fun observarTodos(): Flow<List<Treino>> = treinoDao.observarTodos()

    fun contarTodos(): Flow<Int> = treinoDao.contarTodos()

    suspend fun inserir(treino: Treino) = treinoDao.inserir(treino)

    suspend fun atualizar(treino: Treino) = treinoDao.atualizar(treino)

    suspend fun excluir(treino: Treino) = treinoDao.excluir(treino)
}
