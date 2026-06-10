package com.example.appacademia.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.appacademia.ui.screens.AlunosScreen
import com.example.appacademia.ui.screens.InicioScreen
import com.example.appacademia.ui.screens.PagamentosScreen
import com.example.appacademia.ui.screens.PlanosScreen
import com.example.appacademia.ui.screens.SobreScreen
import com.example.appacademia.ui.screens.TreinosScreen
import com.example.appacademia.viewmodel.AlunoViewModel
import com.example.appacademia.viewmodel.DashboardViewModel
import com.example.appacademia.viewmodel.PagamentoViewModel
import com.example.appacademia.viewmodel.PlanoViewModel
import com.example.appacademia.viewmodel.TreinoViewModel
import com.example.appacademia.viewmodel.ViewModelFactory

@Composable
fun AcademiaNavHost(
    navController: NavHostController,
    factory: ViewModelFactory
) {
    NavHost(
        navController = navController,
        startDestination = Rotas.INICIO
    ) {
        composable(Rotas.INICIO) {
            val viewModel: DashboardViewModel = viewModel(factory = factory)
            InicioScreen(
                viewModel = viewModel,
                onNavegar = { rota -> navController.navigate(rota) }
            )
        }
        composable(Rotas.ALUNOS) {
            val viewModel: AlunoViewModel = viewModel(factory = factory)
            AlunosScreen(
                viewModel = viewModel,
                onVoltar = { navController.popBackStack() }
            )
        }
        composable(Rotas.PLANOS) {
            val viewModel: PlanoViewModel = viewModel(factory = factory)
            PlanosScreen(
                viewModel = viewModel,
                onVoltar = { navController.popBackStack() }
            )
        }
        composable(Rotas.TREINOS) {
            val viewModel: TreinoViewModel = viewModel(factory = factory)
            TreinosScreen(
                viewModel = viewModel,
                onVoltar = { navController.popBackStack() }
            )
        }
        composable(Rotas.PAGAMENTOS) {
            val viewModel: PagamentoViewModel = viewModel(factory = factory)
            PagamentosScreen(
                viewModel = viewModel,
                onVoltar = { navController.popBackStack() }
            )
        }
        composable(Rotas.SOBRE) {
            SobreScreen(onVoltar = { navController.popBackStack() })
        }
    }
}
