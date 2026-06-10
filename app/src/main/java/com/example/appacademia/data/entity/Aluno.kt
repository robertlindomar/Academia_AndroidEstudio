package com.example.appacademia.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alunos")
data class Aluno(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nome: String,
    val cpf: String,
    val telefone: String = "",
    val email: String = "",
    val ativo: Boolean = true,
    val dataCadastro: Long = System.currentTimeMillis()
)
