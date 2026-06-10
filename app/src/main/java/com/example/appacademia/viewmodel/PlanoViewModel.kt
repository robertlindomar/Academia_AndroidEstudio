package com.example.appacademia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appacademia.data.entity.Plano
import com.example.appacademia.data.repository.PlanoRepositorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PlanoUiEstado(
    val planos: List<Plano> = emptyList(),
    val nome: String = "",
    val descricao: String = "",
    val valorTexto: String = "",
    val duracaoTexto: String = "30",
    val mensagemErro: String? = null,
    val exibirDialogo: Boolean = false,
    val editandoId: Long? = null,
    val exibirDialogoExclusao: Boolean = false,
    val itemParaExcluir: Plano? = null
)

class PlanoViewModel(private val planoRepositorio: PlanoRepositorio) : ViewModel() {

    private val _estado = MutableStateFlow(PlanoUiEstado())
    val estado: StateFlow<PlanoUiEstado> = _estado.asStateFlow()

    init {
        viewModelScope.launch {
            planoRepositorio.observarTodos().collect { lista ->
                _estado.update { it.copy(planos = lista) }
            }
        }
    }

    fun abrirDialogo() {
        _estado.update {
            it.copy(
                exibirDialogo = true,
                editandoId = null,
                nome = "",
                descricao = "",
                valorTexto = "",
                duracaoTexto = "30",
                mensagemErro = null
            )
        }
    }

    fun abrirEdicao(plano: Plano) {
        _estado.update {
            it.copy(
                exibirDialogo = true,
                editandoId = plano.id,
                nome = plano.nome,
                descricao = plano.descricao,
                valorTexto = plano.valor.toString(),
                duracaoTexto = plano.duracaoEmDias.toString(),
                mensagemErro = null
            )
        }
    }

    fun fecharDialogo() {
        _estado.update {
            it.copy(exibirDialogo = false, editandoId = null, mensagemErro = null)
        }
    }

    fun atualizarNome(valor: String) {
        _estado.update { it.copy(nome = valor, mensagemErro = null) }
    }

    fun atualizarDescricao(valor: String) {
        _estado.update { it.copy(descricao = valor) }
    }

    fun atualizarValor(valor: String) {
        _estado.update { it.copy(valorTexto = valor, mensagemErro = null) }
    }

    fun atualizarDuracao(valor: String) {
        _estado.update { it.copy(duracaoTexto = valor) }
    }

    fun salvar() {
        val estadoAtual = _estado.value
        if (estadoAtual.nome.isBlank()) {
            _estado.update { it.copy(mensagemErro = "Nome é obrigatório") }
            return
        }
        val valor = estadoAtual.valorTexto.replace(",", ".").toDoubleOrNull()
        if (valor == null || valor <= 0) {
            _estado.update { it.copy(mensagemErro = "Valor deve ser maior que zero") }
            return
        }
        val duracao = estadoAtual.duracaoTexto.toIntOrNull() ?: 30
        viewModelScope.launch {
            val plano = Plano(
                id = estadoAtual.editandoId ?: 0,
                nome = estadoAtual.nome.trim(),
                descricao = estadoAtual.descricao.trim(),
                valor = valor,
                duracaoEmDias = duracao
            )
            if (estadoAtual.editandoId != null) {
                planoRepositorio.atualizar(plano)
            } else {
                planoRepositorio.inserir(plano)
            }
            fecharDialogo()
        }
    }

    fun solicitarExclusao(plano: Plano) {
        _estado.update { it.copy(exibirDialogoExclusao = true, itemParaExcluir = plano) }
    }

    fun confirmarExclusao() {
        val plano = _estado.value.itemParaExcluir ?: return
        viewModelScope.launch {
            planoRepositorio.excluir(plano)
            cancelarExclusao()
        }
    }

    fun cancelarExclusao() {
        _estado.update { it.copy(exibirDialogoExclusao = false, itemParaExcluir = null) }
    }
}
