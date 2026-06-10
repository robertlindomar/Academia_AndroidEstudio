# App Academia

Aplicativo Android para gerenciamento de academia, desenvolvido com **Kotlin** e **Jetpack Compose**.

## Funcionalidades

- Dashboard com resumo (alunos, planos, treinos e pagamentos pendentes)
- Cadastro, listagem, edição e exclusão de **alunos**
- Cadastro, listagem, edição e exclusão de **planos**
- Cadastro, listagem, edição e exclusão de **treinos** (vinculados a alunos)
- Cadastro, listagem, edição e exclusão de **pagamentos** (vinculados a alunos)
- Marcar pagamentos como pago
- Destaque visual para alunos inativos e pagamentos atrasados
- Validação de campos obrigatórios
- Tela **Sobre** com informações do projeto
- Suporte a **Dark Mode**

## Tecnologias

- Kotlin
- Jetpack Compose
- Material Design 3
- Navigation Compose
- MVVM
- Room Database
- StateFlow
- Coroutines

## Arquitetura

```
UI (Compose) → ViewModel → Repository → DAO → Room (SQLite)
```

- **UI:** telas e componentes Compose
- **ViewModel:** estado da tela e regras de negócio
- **Repository:** abstração de acesso aos dados
- **DAO / Room:** persistência local no dispositivo

## Estrutura do projeto

```
app/src/main/java/com/example/appacademia/
├── data/          # entities, dao, database, repository
├── viewmodel/     # ViewModels e UiEstado
├── ui/            # screens, components, navigation, theme
└── util/          # formatadores e helpers
```

## Como executar

1. Clone o repositório:
   ```bash
   git clone <url-do-repositorio>
   cd AppAcademia2
   ```
2. Abra o projeto no **Android Studio** (Ladybug ou superior recomendado).
3. Aguarde o Gradle sincronizar as dependências.
4. Execute em um emulador ou dispositivo físico (API 24+).

> O arquivo `local.properties` é gerado automaticamente pelo Android Studio com o caminho do SDK e **não** deve ser enviado ao GitHub.

## Requisitos

- Android Studio
- JDK 11+
- minSdk: 24
- targetSdk: 36

## Desenvolvedor

**Robert Lindomar Fernandes De Sousa**

## Licença

Projeto acadêmico — uso educacional.
