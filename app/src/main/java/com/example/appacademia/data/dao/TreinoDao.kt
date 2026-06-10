package com.example.appacademia.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.appacademia.data.entity.Treino
import kotlinx.coroutines.flow.Flow

@Dao
interface TreinoDao {

    @Query("SELECT * FROM treinos ORDER BY dataInicio DESC")
    fun observarTodos(): Flow<List<Treino>>

    @Query("SELECT COUNT(*) FROM treinos")
    fun contarTodos(): Flow<Int>

    @Insert
    suspend fun inserir(treino: Treino): Long

    @Update
    suspend fun atualizar(treino: Treino)

    @Delete
    suspend fun excluir(treino: Treino)

    @Query("DELETE FROM treinos WHERE alunoId = :alunoId")
    suspend fun excluirPorAluno(alunoId: Long)
}
