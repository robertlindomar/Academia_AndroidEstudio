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
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.appacademia.data.entity.Pagamento
import com.example.appacademia.ui.components.AcademiaTopBar
import com.example.appacademia.ui.components.BotoesAcaoCard
import com.example.appacademia.ui.components.CampoTexto
import com.example.appacademia.ui.components.ChipStatus
import com.example.appacademia.ui.components.DialogConfirmacaoExclusao
import com.example.appacademia.ui.components.EstadoVazio
import com.example.appacademia.ui.components.FabAcademia
import com.example.appacademia.ui.components.FundoTela
import com.example.appacademia.ui.theme.CorAtrasado
import com.example.appacademia.ui.theme.CorPago
import com.example.appacademia.ui.theme.CorPendente
import com.example.appacademia.util.formatarData
import com.example.appacademia.util.formatarMoeda
import com.example.appacademia.util.pagamentoEstaAtrasado
import com.example.appacademia.viewmodel.PagamentoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagamentosScreen(
    viewModel: PagamentoViewModel,
    onVoltar: () -> Unit
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { AcademiaTopBar(titulo = "Pagamentos", onVoltar = onVoltar) },
        floatingActionButton = {
            FabAcademia(
                icone = Icons.Default.Add,
                descricao = "Novo pagamento",
                onClick = { viewModel.abrirDialogo() }
            )
        }
    ) { padding ->
        FundoTela {
            if (estado.pagamentos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    EstadoVazio(
                        icone = Icons.Default.Payments,
                        titulo = "Nenhum pagamento cadastrado",
                        subtitulo = "Toque no + para registrar um pagamento"
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
                    items(estado.pagamentos, key = { it.id }) { pagamento ->
                        CardPagamento(
                            pagamento = pagamento,
                            onMarcarPago = { viewModel.marcarComoPago(pagamento) },
                            onEditar = { viewModel.abrirEdicao(pagamento) },
                            onExcluir = { viewModel.solicitarExclusao(pagamento) }
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
            ?: estado.pagamentos.find { it.id == estado.editandoId }?.nomeAluno
            ?: "Selecione o aluno"

        AlertDialog(
            onDismissRequest = { viewModel.fecharDialogo() },
            title = {
                Text(if (estado.editandoId != null) "Editar pagamento" else "Novo pagamento")
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
                    CampoTexto("Valor (R$) *", estado.valorTexto, viewModel::atualizarValor)
                    CampoTexto(
                        "Vence em (dias)",
                        estado.diasVencimentoTexto,
                        viewModel::atualizarDiasVencimento
                    )
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
        val pagamento = estado.itemParaExcluir
        if (pagamento != null) {
            DialogConfirmacaoExclusao(
                titulo = "Excluir pagamento",
                mensagem = "Excluir pagamento de ${pagamento.nomeAluno}?",
                onConfirmar = { viewModel.confirmarExclusao() },
                onCancelar = { viewModel.cancelarExclusao() }
            )
        }
    }
}

@Composable
private fun CardPagamento(
    pagamento: Pagamento,
    onMarcarPago: () -> Unit,
    onEditar: () -> Unit,
    onExcluir: () -> Unit
) {
    val atrasado = pagamentoEstaAtrasado(pagamento.dataVencimento, pagamento.pago)
    val corContainer = when {
        pagamento.pago -> MaterialTheme.colorScheme.surface
        atrasado -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
        else -> MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f)
    }
    val corStatus = when {
        pagamento.pago -> CorPago
        atrasado -> CorAtrasado
        else -> CorPendente
    }
    val textoStatus = when {
        pagamento.pago -> "Pago"
        atrasado -> "Atrasado"
        else -> "Pendente"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = corContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        pagamento.nomeAluno,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        formatarMoeda(pagamento.valor),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        "Vencimento: ${formatarData(pagamento.dataVencimento)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    ChipStatus(
                        texto = textoStatus,
                        corTexto = corStatus,
                        corFundo = corStatus.copy(alpha = 0.15f),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                BotoesAcaoCard(onEditar = onEditar, onExcluir = onExcluir)
            }
            if (!pagamento.pago) {
                Button(
                    onClick = onMarcarPago,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Marcar como pago")
                }
            }
        }
    }
}
