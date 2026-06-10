package com.example.appacademia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appacademia.data.entity.Aluno
import com.example.appacademia.data.entity.Treino
import com.example.appacademia.data.repository.AlunoRepositorio
import com.example.appacademia.data.repository.TreinoRepositorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TreinoUiEstado(
    val treinos: List<Treino> = emptyList(),
    val alunosAtivos: List<Aluno> = emptyList(),
    val alunoSelecionadoId: Long? = null,
    val descricao: String = "",
    val objetivo: String = "",
    val mensagemErro: String? = null,
    val exibirDialogo: Boolean = false,
    val editandoId: Long? = null,
    val exibirDialogoExclusao: Boolean = false,
    val itemParaExcluir: Treino? = null
)

class TreinoViewModel(
    private val treinoRepositorio: TreinoRepositorio,
    private val alunoRepositorio: AlunoRepositorio
) : ViewModel() {

    private val _estado = MutableStateFlow(TreinoUiEstado())
    val estado: StateFlow<TreinoUiEstado> = _estado.asStateFlow()

    init {
        viewModelScope.launch {
            treinoRepositorio.observarTodos().collect { lista ->
                _estado.update { it.copy(treinos = lista) }
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
                descricao = "",
                objetivo = "",
                mensagemErro = null
            )
        }
    }

    fun abrirEdicao(treino: Treino) {
        _estado.update {
            it.copy(
                exibirDialogo = true,
                editandoId = treino.id,
                alunoSelecionadoId = treino.alunoId,
                descricao = treino.descricao,
                objetivo = treino.objetivo,
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

    fun atualizarDescricao(valor: String) {
        _estado.update { it.copy(descricao = valor, mensagemErro = null) }
    }

    fun atualizarObjetivo(valor: String) {
        _estado.update { it.copy(objetivo = valor, mensagemErro = null) }
    }

    fun salvar() {
        val estadoAtual = _estado.value
        val alunoId = estadoAtual.alunoSelecionadoId
        if (alunoId == null) {
            _estado.update { it.copy(mensagemErro = "Selecione um aluno") }
            return
        }
        if (estadoAtual.descricao.isBlank() && estadoAtual.objetivo.isBlank()) {
            _estado.update { it.copy(mensagemErro = "Informe descrição ou objetivo") }
            return
        }
        val aluno = estadoAtual.alunosAtivos.find { it.id == alunoId }
            ?: estadoAtual.treinos.find { it.alunoId == alunoId }?.let { treino ->
                Aluno(id = treino.alunoId, nome = treino.nomeAluno, cpf = "")
            }
            ?: return
        viewModelScope.launch {
            val treinoExistente = estadoAtual.editandoId?.let { id ->
                estadoAtual.treinos.find { it.id == id }
            }
            val treino = Treino(
                id = estadoAtual.editandoId ?: 0,
                alunoId = aluno.id,
                nomeAluno = aluno.nome,
                descricao = estadoAtual.descricao.trim(),
                objetivo = estadoAtual.objetivo.trim(),
                dataInicio = treinoExistente?.dataInicio ?: System.currentTimeMillis()
            )
            if (estadoAtual.editandoId != null) {
                treinoRepositorio.atualizar(treino)
            } else {
                treinoRepositorio.inserir(treino)
            }
            fecharDialogo()
        }
    }

    fun solicitarExclusao(treino: Treino) {
        _estado.update { it.copy(exibirDialogoExclusao = true, itemParaExcluir = treino) }
    }

    fun confirmarExclusao() {
        val treino = _estado.value.itemParaExcluir ?: return
        viewModelScope.launch {
            treinoRepositorio.excluir(treino)
            cancelarExclusao()
        }
    }

    fun cancelarExclusao() {
        _estado.update { it.copy(exibirDialogoExclusao = false, itemParaExcluir = null) }
    }
}
