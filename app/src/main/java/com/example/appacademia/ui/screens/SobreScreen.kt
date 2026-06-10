package com.example.appacademia.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Architecture
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appacademia.ui.components.AcademiaTopBar
import com.example.appacademia.ui.components.FundoTela

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SobreScreen(onVoltar: () -> Unit) {
    Scaffold(
        topBar = { AcademiaTopBar(titulo = "Sobre", onVoltar = onVoltar) }
    ) { padding ->
        FundoTela {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Icon(
                            imageVector = Icons.Default.FitnessCenter,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "App Academia",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Gerenciamento completo de academia",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                SecaoSobre(
                    icone = Icons.Default.FitnessCenter,
                    titulo = "Objetivo",
                    conteudo = "Aplicativo mobile para gerenciamento individual de academia, com cadastro e consulta de alunos, planos, treinos e pagamentos."
                )

                SecaoSobre(
                    icone = Icons.Default.Code,
                    titulo = "Tecnologias",
                    conteudo = "Kotlin • Jetpack Compose • Material Design 3\nNavigation Compose • MVVM • Room Database • StateFlow"
                )

                SecaoSobre(
                    icone = Icons.Default.Architecture,
                    titulo = "Arquitetura MVVM",
                    conteudo = "A tela (View) observa o ViewModel via StateFlow. O ViewModel valida dados e chama o Repository. O Repository usa o DAO para ler/gravar no Room (SQLite local)."
                )

                SecaoSobre(
                    icone = Icons.Default.AccountCircle,
                    titulo = "Desenvolvedor",
                    conteudo = "Robert Lindomar Fernandes De Sousa"
                )
            }
        }
    }
}

@Composable
private fun SecaoSobre(
    icone: ImageVector,
    titulo: String,
    conteudo: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(
                imageVector = icone,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = conteudo,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 6.dp)
            )
        }
    }
}
