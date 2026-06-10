package com.example.appacademia.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appacademia.data.entity.Treino
import com.example.appacademia.ui.components.AcademiaTopBar
import com.example.appacademia.ui.components.BotoesAcaoCard
import com.example.appacademia.ui.components.CampoTexto
import com.example.appacademia.ui.components.ChipStatus
import com.example.appacademia.ui.components.DialogConfirmacaoExclusao
import com.example.appacademia.ui.components.EstadoVazio
import com.example.appacademia.ui.components.FabAcademia
import com.example.appacademia.ui.components.FundoTela
import com.example.appacademia.ui.theme.AzulPrimario
import com.example.appacademia.util.formatarData
import com.example.appacademia.viewmodel.TreinoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TreinosScreen(
    viewModel: TreinoViewModel,
    onVoltar: () -> Unit
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { AcademiaTopBar(titulo = "Treinos", onVoltar = onVoltar) },
        floatingActionButton = {
            FabAcademia(
                icone = Icons.Default.Add,
                descricao = "Novo treino",
                onClick = { viewModel.abrirDialogo() }
            )
        }
    ) { padding ->
        FundoTela {
            if (estado.treinos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    EstadoVazio(
                        icone = Icons.Default.FitnessCenter,
                        titulo = "Nenhum treino cadastrado",
                        subtitulo = "Toque no + para registrar um treino"
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 12.dp)
                ) {
                    items(estado.treinos, key = { it.id }) { treino ->
                        CardTreino(
                            treino = treino,
                            onEditar = { viewModel.abrirEdicao(treino) },
                            onExcluir = { viewModel.solicitarExclusao(treino) }
                        )
                    }
                }
            }
        }
    }

    if (estado.exibirDialogo) {
        var menuExpandido by remember { mutableStateOf(false) }
        val alunoSelecionado = estado.alunosAtivos.find { it.id == estado.alunoSelecionadoId }
        val nomeAlunoExibido = alunoSelecionado?.nome
            ?: estado.treinos.find { it.id == estado.editandoId }?.nomeAluno
            ?: "Selecione o aluno"

        AlertDialog(
            onDismissRequest = { viewModel.fecharDialogo() },
            title = {
                Text(if (estado.editandoId != null) "Editar treino" else "Novo treino")
            },
            text = {
                Column {
                    ExposedDropdownMenuBox(
                        expanded = menuExpandido,
                        onExpandedChange = { menuExpandido = it }
                    ) {
                        OutlinedTextField(
                            value = nomeAlunoExibido,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Aluno *") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(menuExpandido) },
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )
                        DropdownMenu(
                            expanded = menuExpandido,
                            onDismissRequest = { menuExpandido = false }
                        ) {
                            estado.alunosAtivos.forEach { aluno ->
                                DropdownMenuItem(
                                    text = { Text(aluno.nome) },
                                    onClick = {
                                        viewModel.selecionarAluno(aluno.id)
                                        menuExpandido = false
                                    }
                                )
                            }
                        }
                    }
                    CampoTexto("Descrição", estado.descricao, viewModel::atualizarDescricao)
                    CampoTexto("Objetivo", estado.objetivo, viewModel::atualizarObjetivo)
                    estado.mensagemErro?.let { mensagem ->
                        Text(
                            text = mensagem,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.salvar() }) { Text("Salvar") }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.fecharDialogo() }) { Text("Cancelar") }
            }
        )
    }

    if (estado.exibirDialogoExclusao) {
        val treino = estado.itemParaExcluir
        if (treino != null) {
            DialogConfirmacaoExclusao(
                titulo = "Excluir treino",
                mensagem = "Excluir treino de ${treino.nomeAluno}?",
                onConfirmar = { viewModel.confirmarExclusao() },
                onCancelar = { viewModel.cancelarExclusao() }
            )
        }
    }
}

@Composable
private fun CardTreino(
    treino: Treino,
    onEditar: () -> Unit,
    onExcluir: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    treino.nomeAluno,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                if (treino.objetivo.isNotBlank()) {
                    Text(
                        "Objetivo: ${treino.objetivo}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                if (treino.descricao.isNotBlank()) {
                    Text(
                        treino.descricao,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
                ChipStatus(
                    texto = "Início: ${formatarData(treino.dataInicio)}",
                    corTexto = AzulPrimario,
                    corFundo = AzulPrimario.copy(alpha = 0.1f),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            BotoesAcaoCard(onEditar = onEditar, onExcluir = onExcluir)
        }
    }
}
