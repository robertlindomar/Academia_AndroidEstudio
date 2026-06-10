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
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appacademia.data.entity.Aluno
import com.example.appacademia.ui.components.AcademiaTopBar
import com.example.appacademia.ui.components.BotoesAcaoCard
import com.example.appacademia.ui.components.CampoTexto
import com.example.appacademia.ui.components.ChipStatus
import com.example.appacademia.ui.components.DialogConfirmacaoExclusao
import com.example.appacademia.ui.components.EstadoVazio
import com.example.appacademia.ui.components.FabAcademia
import com.example.appacademia.ui.components.FundoTela
import com.example.appacademia.ui.theme.CorAtivo
import com.example.appacademia.ui.theme.CorInativo
import com.example.appacademia.util.formatarData
import com.example.appacademia.viewmodel.AlunoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlunosScreen(
    viewModel: AlunoViewModel,
    onVoltar: () -> Unit
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { AcademiaTopBar(titulo = "Alunos", onVoltar = onVoltar) },
        floatingActionButton = {
            FabAcademia(
                icone = Icons.Default.Add,
                descricao = "Novo aluno",
                onClick = { viewModel.abrirDialogo() }
            )
        }
    ) { padding ->
        FundoTela {
            if (estado.alunos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    EstadoVazio(
                        icone = Icons.Default.People,
                        titulo = "Nenhum aluno cadastrado",
                        subtitulo = "Toque no + para adicionar o primeiro aluno"
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
                    items(estado.alunos, key = { it.id }) { aluno ->
                        CardAluno(
                            aluno = aluno,
                            onAlternarAtivo = { viewModel.alternarAtivo(aluno) },
                            onEditar = { viewModel.abrirEdicao(aluno) },
                            onExcluir = { viewModel.solicitarExclusao(aluno) }
                        )
                    }
                }
            }
        }
    }

    if (estado.exibirDialogo) {
        AlertDialog(
            onDismissRequest = { viewModel.fecharDialogo() },
            title = {
                Text(if (estado.editandoId != null) "Editar aluno" else "Novo aluno")
            },
            text = {
                Column {
                    CampoTexto("Nome *", estado.nome, viewModel::atualizarNome)
                    CampoTexto("CPF *", estado.cpf, viewModel::atualizarCpf)
                    CampoTexto("Telefone", estado.telefone, viewModel::atualizarTelefone)
                    CampoTexto("E-mail", estado.email, viewModel::atualizarEmail)
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
                TextButton(onClick = { viewModel.salvar() }) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.fecharDialogo() }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (estado.exibirDialogoExclusao) {
        val aluno = estado.itemParaExcluir
        if (aluno != null) {
            DialogConfirmacaoExclusao(
                titulo = "Excluir aluno",
                mensagem = "Excluir ${aluno.nome}? Treinos e pagamentos vinculados também serão removidos.",
                onConfirmar = { viewModel.confirmarExclusao() },
                onCancelar = { viewModel.cancelarExclusao() }
            )
        }
    }
}

@Composable
private fun CardAluno(
    aluno: Aluno,
    onAlternarAtivo: () -> Unit,
    onEditar: () -> Unit,
    onExcluir: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (aluno.ativo) {
                MaterialTheme.colorScheme.surface
            } else {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
            }
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = aluno.nome,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = if (aluno.ativo) MaterialTheme.colorScheme.onSurface else CorInativo
                    )
                    Text(
                        "CPF: ${aluno.cpf}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (aluno.telefone.isNotBlank()) {
                        Text(
                            "Tel: ${aluno.telefone}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        "Cadastro: ${formatarData(aluno.dataCadastro)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                BotoesAcaoCard(onEditar = onEditar, onExcluir = onExcluir)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ChipStatus(
                    texto = if (aluno.ativo) "Ativo" else "Inativo",
                    corTexto = if (aluno.ativo) CorAtivo else CorInativo,
                    corFundo = if (aluno.ativo) {
                        CorAtivo.copy(alpha = 0.12f)
                    } else {
                        CorInativo.copy(alpha = 0.12f)
                    }
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Ativo",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(end = 6.dp)
                    )
                    Switch(
                        checked = aluno.ativo,
                        onCheckedChange = { onAlternarAtivo() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                            checkedTrackColor = CorAtivo
                        )
                    )
                }
            }
        }
    }
}
