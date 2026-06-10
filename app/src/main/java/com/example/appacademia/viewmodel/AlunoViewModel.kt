package com.example.appacademia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appacademia.data.entity.Aluno
import com.example.appacademia.data.repository.AlunoRepositorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AlunoUiEstado(
    val alunos: List<Aluno> = emptyList(),
    val nome: String = "",
    val cpf: String = "",
    val telefone: String = "",
    val email: String = "",
    val mensagemErro: String? = null,
    val exibirDialogo: Boolean = false,
    val editandoId: Long? = null,
    val exibirDialogoExclusao: Boolean = false,
    val itemParaExcluir: Aluno? = null
)

/**
 * ViewModel: prepara dados e regras para a tela, sobrevive a mudanças de configuração.
 * O StateFlow emite o estado atual; quando muda, a UI Compose recompõe automaticamente.
 */
class AlunoViewModel(private val alunoRepositorio: AlunoRepositorio) : ViewModel() {

    private val _estado = MutableStateFlow(AlunoUiEstado())
    val estado: StateFlow<AlunoUiEstado> = _estado.asStateFlow()

    init {
        viewModelScope.launch {
            alunoRepositorio.observarTodos().collect { lista ->
                _estado.update { it.copy(alunos = lista) }
            }
        }
    }

    fun abrirDialogo() {
        _estado.update {
            it.copy(
                exibirDialogo = true,
                editandoId = null,
                nome = "",
                cpf = "",
                telefone = "",
                email = "",
                mensagemErro = null
            )
        }
    }

    fun abrirEdicao(aluno: Aluno) {
        _estado.update {
            it.copy(
                exibirDialogo = true,
                editandoId = aluno.id,
                nome = aluno.nome,
                cpf = aluno.cpf,
                telefone = aluno.telefone,
                email = aluno.email,
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

    fun atualizarCpf(valor: String) {
        _estado.update { it.copy(cpf = valor, mensagemErro = null) }
    }

    fun atualizarTelefone(valor: String) {
        _estado.update { it.copy(telefone = valor) }
    }

    fun atualizarEmail(valor: String) {
        _estado.update { it.copy(email = valor) }
    }

    fun salvar() {
        val estadoAtual = _estado.value
        when {
            estadoAtual.nome.isBlank() -> {
                _estado.update { it.copy(mensagemErro = "Nome é obrigatório") }
                return
            }
            estadoAtual.cpf.isBlank() -> {
                _estado.update { it.copy(mensagemErro = "CPF é obrigatório") }
                return
            }
        }
        viewModelScope.launch {
            val alunoExistente = estadoAtual.editandoId?.let { id ->
                estadoAtual.alunos.find { it.id == id }
            }
            val aluno = Aluno(
                id = estadoAtual.editandoId ?: 0,
                nome = estadoAtual.nome.trim(),
                cpf = estadoAtual.cpf.trim(),
                telefone = estadoAtual.telefone.trim(),
                email = estadoAtual.email.trim(),
                ativo = alunoExistente?.ativo ?: true,
                dataCadastro = alunoExistente?.dataCadastro ?: System.currentTimeMillis()
            )
            if (estadoAtual.editandoId != null) {
                alunoRepositorio.atualizar(aluno)
            } else {
                alunoRepositorio.inserir(aluno)
            }
            fecharDialogo()
        }
    }

    fun alternarAtivo(aluno: Aluno) {
        viewModelScope.launch {
            alunoRepositorio.alternarAtivo(aluno)
        }
    }

    fun solicitarExclusao(aluno: Aluno) {
        _estado.update { it.copy(exibirDialogoExclusao = true, itemParaExcluir = aluno) }
    }

    fun confirmarExclusao() {
        val aluno = _estado.value.itemParaExcluir ?: return
        viewModelScope.launch {
            alunoRepositorio.excluir(aluno)
            cancelarExclusao()
        }
    }

    fun cancelarExclusao() {
        _estado.update { it.copy(exibirDialogoExclusao = false, itemParaExcluir = null) }
    }
}
