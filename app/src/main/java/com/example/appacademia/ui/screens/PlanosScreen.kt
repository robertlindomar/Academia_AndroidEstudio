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
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appacademia.data.entity.Plano
import com.example.appacademia.ui.components.AcademiaTopBar
import com.example.appacademia.ui.components.BotoesAcaoCard
import com.example.appacademia.ui.components.CampoTexto
import com.example.appacademia.ui.components.ChipStatus
import com.example.appacademia.ui.components.DialogConfirmacaoExclusao
import com.example.appacademia.ui.components.EstadoVazio
import com.example.appacademia.ui.components.FabAcademia
import com.example.appacademia.ui.components.FundoTela
import com.example.appacademia.ui.theme.LaranjaAccent
import com.example.appacademia.util.formatarMoeda
import com.example.appacademia.viewmodel.PlanoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanosScreen(
    viewModel: PlanoViewModel,
    onVoltar: () -> Unit
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { AcademiaTopBar(titulo = "Planos", onVoltar = onVoltar) },
        floatingActionButton = {
            FabAcademia(
                icone = Icons.Default.Add,
                descricao = "Novo plano",
                onClick = { viewModel.abrirDialogo() }
            )
        }
    ) { padding ->
        FundoTela {
            if (estado.planos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    EstadoVazio(
                        icone = Icons.Default.CardMembership,
                        titulo = "Nenhum plano cadastrado",
                        subtitulo = "Toque no + para criar um plano"
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
                    items(estado.planos, key = { it.id }) { plano ->
                        CardPlano(
                            plano = plano,
                            onEditar = { viewModel.abrirEdicao(plano) },
                            onExcluir = { viewModel.solicitarExclusao(plano) }
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
                Text(if (estado.editandoId != null) "Editar plano" else "Novo plano")
            },
            text = {
                Column {
                    CampoTexto("Nome *", estado.nome, viewModel::atualizarNome)
                    CampoTexto("Descrição", estado.descricao, viewModel::atualizarDescricao)
                    CampoTexto("Valor (R$) *", estado.valorTexto, viewModel::atualizarValor)
                    CampoTexto("Duração (dias)", estado.duracaoTexto, viewModel::atualizarDuracao)
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
        val plano = estado.itemParaExcluir
        if (plano != null) {
            DialogConfirmacaoExclusao(
                titulo = "Excluir plano",
                mensagem = "Excluir ${plano.nome}?",
                onConfirmar = { viewModel.confirmarExclusao() },
                onCancelar = { viewModel.cancelarExclusao() }
            )
        }
    }
}

@Composable
private fun CardPlano(
    plano: Plano,
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
                    plano.nome,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                if (plano.descricao.isNotBlank()) {
                    Text(
                        plano.descricao,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Text(
                    text = formatarMoeda(plano.valor),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
                ChipStatus(
                    texto = "${plano.duracaoEmDias} dias",
                    corTexto = LaranjaAccent,
                    corFundo = LaranjaAccent.copy(alpha = 0.12f),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            BotoesAcaoCard(onEditar = onEditar, onExcluir = onExcluir)
        }
    }
}
