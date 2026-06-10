package com.example.appacademia.data.repository

import com.example.appacademia.data.dao.PlanoDao
import com.example.appacademia.data.entity.Plano
import kotlinx.coroutines.flow.Flow

class PlanoRepositorio(private val planoDao: PlanoDao) {

    fun observarTodos(): Flow<List<Plano>> = planoDao.observarTodos()

    fun contarTodos(): Flow<Int> = planoDao.contarTodos()

    suspend fun inserir(plano: Plano) = planoDao.inserir(plano)

    suspend fun atualizar(plano: Plano) = planoDao.atualizar(plano)

    suspend fun excluir(plano: Plano) = planoDao.excluir(plano)
}
