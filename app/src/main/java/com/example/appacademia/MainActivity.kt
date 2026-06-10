package com.example.appacademia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.appacademia.ui.navigation.AcademiaNavHost
import com.example.appacademia.ui.theme.AppAcademiaTheme
import com.example.appacademia.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val aplicacao = application as AcademiaApplication
        val factory = ViewModelFactory.criar(aplicacao)

        setContent {
            AppAcademiaTheme {
                val navController = rememberNavController()
                AcademiaNavHost(
                    navController = navController,
                    factory = remember { factory }
                )
            }
        }
    }
}
