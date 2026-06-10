package com.example.appacademia.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.appacademia.data.entity.Aluno
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object): interface que o Room transforma em consultas SQL.
 * Cada método aqui executa operações no banco SQLite local do dispositivo.
 */
@Dao
interface AlunoDao {

    @Query("SELECT * FROM alunos ORDER BY nome ASC")
    fun observarTodos(): Flow<List<Aluno>>

    @Query("SELECT * FROM alunos WHERE ativo = 1 ORDER BY nome ASC")
    fun observarAtivos(): Flow<List<Aluno>>

    @Query("SELECT COUNT(*) FROM alunos")
    fun contarTodos(): Flow<Int>

    @Insert
    suspend fun inserir(aluno: Aluno): Long

    @Update
    suspend fun atualizar(aluno: Aluno)

    @Delete
    suspend fun excluir(aluno: Aluno)

    @Query("SELECT * FROM alunos WHERE id = :id")
    suspend fun buscarPorId(id: Long): Aluno?
}
