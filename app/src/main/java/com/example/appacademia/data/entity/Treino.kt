package com.example.appacademia.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "treinos")
data class Treino(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val alunoId: Long,
    val nomeAluno: String,
    val descricao: String = "",
    val objetivo: String = "",
    val dataInicio: Long = System.currentTimeMillis()
)
