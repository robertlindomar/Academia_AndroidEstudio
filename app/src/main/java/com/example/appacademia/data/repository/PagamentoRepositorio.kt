package com.example.appacademia.data.repository

import com.example.appacademia.data.dao.PagamentoDao
import com.example.appacademia.data.entity.Pagamento
import kotlinx.coroutines.flow.Flow

class PagamentoRepositorio(private val pagamentoDao: PagamentoDao) {

    fun observarTodos(): Flow<List<Pagamento>> = pagamentoDao.observarTodos()

    fun contarPendentes(): Flow<Int> = pagamentoDao.contarPendentes()

    suspend fun inserir(pagamento: Pagamento) = pagamentoDao.inserir(pagamento)

    suspend fun atualizar(pagamento: Pagamento) = pagamentoDao.atualizar(pagamento)

    suspend fun excluir(pagamento: Pagamento) = pagamentoDao.excluir(pagamento)

    suspend fun marcarComoPago(pagamento: Pagamento) {
        pagamentoDao.atualizar(pagamento.copy(pago = true))
    }
}
