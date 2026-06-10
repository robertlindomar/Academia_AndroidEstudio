package com.example.appacademia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appacademia.data.entity.Aluno
import com.example.appacademia.data.entity.Pagamento
import com.example.appacademia.data.repository.AlunoRepositorio
import com.example.appacademia.data.repository.PagamentoRepositorio
import com.example.appacademia.util.inicioDoDiaAtual
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PagamentoUiEstado(
    val pagamentos: List<Pagamento> = emptyList(),
    val alunosAtivos: List<Aluno> = emptyList(),
    val alunoSelecionadoId: Long? = null,
    val valorTexto: String = "",
    val diasVencimentoTexto: String = "30",
    val mensagemErro: String? = null,
    val exibirDialogo: Boolean = false,
    val editandoId: Long? = null,
    val exibirDialogoExclusao: Boolean = false,
    val itemParaExcluir: Pagamento? = null
)

class PagamentoViewModel(
    private val pagamentoRepositorio: PagamentoRepositorio,
    private val alunoRepositorio: AlunoRepositorio
) : ViewModel() {

    private val _estado = MutableStateFlow(PagamentoUiEstado())
    val estado: StateFlow<PagamentoUiEstado> = _estado.asStateFlow()

    init {
        viewModelScope.launch {
            pagamentoRepositorio.observarTodos().collect { lista ->
                _estado.update { it.copy(pagamentos = lista) }
            }
        }
        viewModelScope.launch {
            alunoRepositorio.observarAtivos().collect { lista ->
                _estado.update { it.copy(alunosAtivos = lista) }
            }
        }
    }

    fun abrirDialogo() {
        _estado.update {
            it.copy(
                exibirDialogo = true,
                editandoId = null,
                alunoSelecionadoId = null,
                valorTexto = "",
                diasVencimentoTexto = "30",
                mensagemErro = null
            )
        }
    }

    fun abrirEdicao(pagamento: Pagamento) {
        val diasRestantes = ((pagamento.dataVencimento - inicioDoDiaAtual()) / (24L * 60 * 60 * 1000))
            .coerceAtLeast(0)
        _estado.update {
            it.copy(
                exibirDialogo = true,
                editandoId = pagamento.id,
                alunoSelecionadoId = pagamento.alunoId,
                valorTexto = pagamento.valor.toString(),
                diasVencimentoTexto = diasRestantes.toString(),
                mensagemErro = null
            )
        }
    }

    fun fecharDialogo() {
        _estado.update {
            it.copy(exibirDialogo = false, editandoId = null, mensagemErro = null)
        }
    }

    fun selecionarAluno(alunoId: Long) {
        _estado.update { it.copy(alunoSelecionadoId = alunoId, mensagemErro = null) }
    }

    fun atualizarValor(valor: String) {
        _estado.update { it.copy(valorTexto = valor, mensagemErro = null) }
    }

    fun atualizarDiasVencimento(valor: String) {
        _estado.update { it.copy(diasVencimentoTexto = valor) }
    }

    fun salvar() {
        val estadoAtual = _estado.value
        val alunoId = estadoAtual.alunoSelecionadoId
        if (alunoId == null) {
            _estado.update { it.copy(mensagemErro = "Selecione um aluno") }
            return
        }
        val valor = estadoAtual.valorTexto.replace(",", ".").toDoubleOrNull()
        if (valor == null || valor <= 0) {
            _estado.update { it.copy(mensagemErro = "Valor deve ser maior que zero") }
            return
        }
        val dias = estadoAtual.diasVencimentoTexto.toIntOrNull() ?: 30
        val dataVencimento = inicioDoDiaAtual() + dias * 24L * 60 * 60 * 1000
        val aluno = estadoAtual.alunosAtivos.find { it.id == alunoId }
            ?: estadoAtual.pagamentos.find { it.alunoId == alunoId }?.let { pagamento ->
                Aluno(id = pagamento.alunoId, nome = pagamento.nomeAluno, cpf = "")
            }
            ?: return
        viewModelScope.launch {
            val pagamentoExistente = estadoAtual.editandoId?.let { id ->
                estadoAtual.pagamentos.find { it.id == id }
            }
            val pagamento = Pagamento(
                id = estadoAtual.editandoId ?: 0,
                alunoId = aluno.id,
                nomeAluno = aluno.nome,
                valor = valor,
                dataVencimento = dataVencimento,
                pago = pagamentoExistente?.pago ?: false
            )
            if (estadoAtual.editandoId != null) {
                pagamentoRepositorio.atualizar(pagamento)
            } else {
                pagamentoRepositorio.inserir(pagamento)
            }
            fecharDialogo()
        }
    }

    fun marcarComoPago(pagamento: Pagamento) {
        viewModelScope.launch {
            pagamentoRepositorio.marcarComoPago(pagamento)
        }
    }

    fun solicitarExclusao(pagamento: Pagamento) {
        _estado.update { it.copy(exibirDialogoExclusao = true, itemParaExcluir = pagamento) }
    }

    fun confirmarExclusao() {
        val pagamento = _estado.value.itemParaExcluir ?: return
        viewModelScope.launch {
            pagamentoRepositorio.excluir(pagamento)
            cancelarExclusao()
        }
    }

    fun cancelarExclusao() {
        _estado.update { it.copy(exibirDialogoExclusao = false, itemParaExcluir = null) }
    }
}
