package com.example.appacademia.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "planos")
data class Plano(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nome: String,
    val descricao: String = "",
    val valor: Double,
    val duracaoEmDias: Int = 30
)
