package com.example.appacademia.util

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val localeBrasil = Locale("pt", "BR")
private val formatoData = SimpleDateFormat("dd/MM/yyyy", localeBrasil)
private val formatoMoeda = NumberFormat.getCurrencyInstance(localeBrasil)

fun formatarData(millis: Long): String = formatoData.format(Date(millis))

fun formatarMoeda(valor: Double): String = formatoMoeda.format(valor)

fun inicioDoDiaAtual(): Long {
    val calendario = java.util.Calendar.getInstance()
    calendario.set(java.util.Calendar.HOUR_OF_DAY, 0)
    calendario.set(java.util.Calendar.MINUTE, 0)
    calendario.set(java.util.Calendar.SECOND, 0)
    calendario.set(java.util.Calendar.MILLISECOND, 0)
    return calendario.timeInMillis
}

fun pagamentoEstaAtrasado(dataVencimento: Long, pago: Boolean): Boolean {
    return !pago && dataVencimento < inicioDoDiaAtual()
}
