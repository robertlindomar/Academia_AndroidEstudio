package com.example.appacademia.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.appacademia.data.entity.Plano
import kotlinx.coroutines.flow.Flow

@Dao
interface PlanoDao {

    @Query("SELECT * FROM planos ORDER BY nome ASC")
    fun observarTodos(): Flow<List<Plano>>

    @Query("SELECT COUNT(*) FROM planos")
    fun contarTodos(): Flow<Int>

    @Insert
    suspend fun inserir(plano: Plano): Long

    @Update
    suspend fun atualizar(plano: Plano)

    @Delete
    suspend fun excluir(plano: Plano)
}
