package com.example.appacademia.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pagamentos")
data class Pagamento(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val alunoId: Long,
    val nomeAluno: String,
    val valor: Double,
    val dataVencimento: Long,
    val pago: Boolean = false
)
