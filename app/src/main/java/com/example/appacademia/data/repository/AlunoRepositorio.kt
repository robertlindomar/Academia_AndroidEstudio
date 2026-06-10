package com.example.appacademia.data.repository

import com.example.appacademia.data.dao.AlunoDao
import com.example.appacademia.data.dao.PagamentoDao
import com.example.appacademia.data.dao.TreinoDao
import com.example.appacademia.data.entity.Aluno
import kotlinx.coroutines.flow.Flow

/**
 * Repository: camada entre ViewModel e DAO.
 * Centraliza o acesso aos dados e esconde detalhes do banco da interface.
 */
class AlunoRepositorio(
    private val alunoDao: AlunoDao,
    private val treinoDao: TreinoDao,
    private val pagamentoDao: PagamentoDao
) {

    fun observarTodos(): Flow<List<Aluno>> = alunoDao.observarTodos()

    fun observarAtivos(): Flow<List<Aluno>> = alunoDao.observarAtivos()

    fun contarTodos(): Flow<Int> = alunoDao.contarTodos()

    suspend fun inserir(aluno: Aluno) = alunoDao.inserir(aluno)

    suspend fun atualizar(aluno: Aluno) = alunoDao.atualizar(aluno)

    suspend fun alternarAtivo(aluno: Aluno) {
        alunoDao.atualizar(aluno.copy(ativo = !aluno.ativo))
    }

    suspend fun excluir(aluno: Aluno) {
        treinoDao.excluirPorAluno(aluno.id)
        pagamentoDao.excluirPorAluno(aluno.id)
        alunoDao.excluir(aluno)
    }
}
