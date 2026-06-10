package com.example.appacademia.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.appacademia.data.entity.Pagamento
import kotlinx.coroutines.flow.Flow

@Dao
interface PagamentoDao {

    @Query("SELECT * FROM pagamentos ORDER BY dataVencimento ASC")
    fun observarTodos(): Flow<List<Pagamento>>

    @Query("SELECT COUNT(*) FROM pagamentos WHERE pago = 0")
    fun contarPendentes(): Flow<Int>

    @Insert
    suspend fun inserir(pagamento: Pagamento): Long

    @Update
    suspend fun atualizar(pagamento: Pagamento)

    @Delete
    suspend fun excluir(pagamento: Pagamento)

    @Query("DELETE FROM pagamentos WHERE alunoId = :alunoId")
    suspend fun excluirPorAluno(alunoId: Long)
}
