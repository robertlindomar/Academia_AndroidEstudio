package com.example.appacademia.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appacademia.ui.components.AcademiaTopBar
import com.example.appacademia.ui.components.CardEstatistica
import com.example.appacademia.ui.components.CardMenu
import com.example.appacademia.ui.components.FundoTela
import com.example.appacademia.ui.navigation.Rotas
import com.example.appacademia.ui.theme.AzulPrimario
import com.example.appacademia.ui.theme.LaranjaAccent
import com.example.appacademia.ui.theme.LaranjaPendente
import com.example.appacademia.ui.theme.VerdeSucesso
import com.example.appacademia.viewmodel.DashboardViewModel

private data class ItemMenu(
    val titulo: String,
    val descricao: String,
    val rota: String,
    val icone: ImageVector,
    val cor: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    viewModel: DashboardViewModel,
    onNavegar: (String) -> Unit
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    val itensMenu = listOf(
        ItemMenu("Alunos", "Cadastro e gestão de alunos", Rotas.ALUNOS, Icons.Default.People, AzulPrimario),
        ItemMenu("Planos", "Planos e valores", Rotas.PLANOS, Icons.Default.CardMembership, VerdeSucesso),
        ItemMenu("Treinos", "Treinos por aluno", Rotas.TREINOS, Icons.Default.FitnessCenter, LaranjaAccent),
        ItemMenu("Pagamentos", "Mensalidades e vencimentos", Rotas.PAGAMENTOS, Icons.Default.Payments, Color(0xFF7B1FA2)),
        ItemMenu("Sobre", "Informações do projeto", Rotas.SOBRE, Icons.Default.Info, Color(0xFF546E7A))
    )

    Scaffold(
        topBar = {
            AcademiaTopBar(
                titulo = "Academia App",
                subtitulo = "Gerencie sua academia com facilidade"
            )
        }
    ) { padding ->
        FundoTela {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                item {
                    Text(
                        text = "Resumo",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CardEstatistica(
                            titulo = "Alunos",
                            valor = estado.totalAlunos,
                            icone = Icons.Default.People,
                            corIcone = AzulPrimario,
                            modifier = Modifier.weight(1f)
                        )
                        CardEstatistica(
                            titulo = "Planos",
                            valor = estado.totalPlanos,
                            icone = Icons.Default.CardMembership,
                            corIcone = VerdeSucesso,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CardEstatistica(
                            titulo = "Treinos",
                            valor = estado.totalTreinos,
                            icone = Icons.Default.FitnessCenter,
                            corIcone = LaranjaAccent,
                            modifier = Modifier.weight(1f)
                        )
                        CardEstatistica(
                            titulo = "Pendentes",
                            valor = estado.totalPagamentosPendentes,
                            icone = Icons.Default.Warning,
                            corIcone = if (estado.totalPagamentosPendentes > 0) LaranjaPendente else VerdeSucesso,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                if (estado.totalPagamentosPendentes > 0) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.7f)
                            )
                        ) {
                            Text(
                                text = "${estado.totalPagamentosPendentes} pagamento(s) pendente(s) — verifique a seção Pagamentos",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(14.dp),
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }
                item {
                    Text(
                        text = "Menu",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )
                }
                items(itensMenu) { item ->
                    CardMenu(
                        titulo = item.titulo,
                        descricao = item.descricao,
                        icone = item.icone,
                        corIcone = item.cor,
                        onClick = { onNavegar(item.rota) }
                    )
                }
            }
        }
    }
}
