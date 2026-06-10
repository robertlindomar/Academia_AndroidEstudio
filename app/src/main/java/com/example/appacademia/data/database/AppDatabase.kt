package com.example.appacademia.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.appacademia.data.dao.AlunoDao
import com.example.appacademia.data.dao.PagamentoDao
import com.example.appacademia.data.dao.PlanoDao
import com.example.appacademia.data.dao.TreinoDao
import com.example.appacademia.data.entity.Aluno
import com.example.appacademia.data.entity.Pagamento
import com.example.appacademia.data.entity.Plano
import com.example.appacademia.data.entity.Treino

/**
 * Room persiste os dados em um arquivo SQLite no armazenamento interno do app.
 * Os dados permanecem salvos mesmo após fechar ou reiniciar o aplicativo.
 */
@Database(
    entities = [Aluno::class, Plano::class, Treino::class, Pagamento::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun alunoDao(): AlunoDao
    abstract fun planoDao(): PlanoDao
    abstract fun treinoDao(): TreinoDao
    abstract fun pagamentoDao(): PagamentoDao

    companion object {
        @Volatile
        private var instancia: AppDatabase? = null

        fun obterInstancia(contexto: Context): AppDatabase {
            return instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    contexto.applicationContext,
                    AppDatabase::class.java,
                    "academia_banco.db"
                ).build().also { instancia = it }
            }
        }
    }
}
