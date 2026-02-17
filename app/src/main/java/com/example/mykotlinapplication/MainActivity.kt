package com.example.mykotlinapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.mykotlinapplication.data.AppDatabase
import com.example.mykotlinapplication.data.Review
import com.example.mykotlinapplication.ui.theme.MyKotlinApplicationTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyKotlinApplicationTheme {

                val context = LocalContext.current
                val db = remember { AppDatabase.getDatabase(context) }
                val userDao = remember { db.userDao() }

                val sessionManager = remember { SessionManager(context) }

                val authVm: AuthViewModel = viewModel(
                    factory = AuthViewModelFactory(userDao, sessionManager)
                )

                val authState by authVm.authState.collectAsState()
                val loginFailed by authVm.loginFailed.collectAsState()
                val isAdmin by authVm.isAdmin.collectAsState()

                var showAdmin by rememberSaveable { mutableStateOf(false) }

                val gamesVm: GamesViewModel = viewModel()
                val random by gamesVm.randomGame.collectAsState()
                val popularGames by gamesVm.popularGames.collectAsState()
                var showGames by rememberSaveable { mutableStateOf(false) }

                when (authState) {

                    AuthState.LOGGED_OUT -> {
                        LoginScreen(
                            onLogin = { u, p -> authVm.login(u, p) },
                            loginFailed = loginFailed
                        )
                    }

                    AuthState.LOGGED_IN -> {
                        LaunchedEffect(Unit) {
                            gamesVm.fetchPopularGames()
                        }

                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
                            floatingActionButton = {
                                if (!showGames && !showAdmin && isAdmin) {
                                    FloatingActionButton(onClick = { showAdmin = true }) {
                                        Text("Admin")
                                    }
                                }
                            }
                        ) { innerPadding ->

                            when {
                                showAdmin -> {
                                    AdminScreen(
                                        userDao = userDao,
                                        onBack = { showAdmin = false },
                                        onSignOut = {
                                            authVm.logout()
                                            showAdmin = false
                                            showGames = false
                                        }
                                    )
                                }

                                showGames -> {
                                    GamesScreen(
                                        modifier = Modifier.padding(innerPadding),
                                        onBack = { showGames = false },
                                        vm = gamesVm
                                    )
                                }

                                else -> {
                                    LandingScreen(
                                        modifier = Modifier.padding(innerPadding),
                                        randomGame = random,
                                        popularGames = popularGames,
                                        onSearchGames = { showGames = true },
                                        onRandomRequested = { gamesVm.pickRandomGame() },
                                        onClearRandom = { gamesVm.clearRandom() },
                                        onSignOut = {
                                            authVm.logout()
                                            showGames = false
                                            showAdmin = false
                                        },
                                        onGameClick = { id ->
                                            gamesVm.openGameDetails(id)
                                            showGames = true
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamesScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    vm: GamesViewModel
) {
    val games by vm.games.collectAsState()
    val error by vm.error.collectAsState()
    val selectedDetails by vm.selectedDescriptionofGame.collectAsState()
    val searchQuery by vm.searchQuery.collectAsState()

    var sortBy by remember { mutableStateOf("alphabetical") }
    var category by remember { mutableStateOf("") }

    Column(modifier = modifier.padding(12.dp)) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "← Back",
                modifier = Modifier.clickable {
                    if (selectedDetails != null) vm.closeGameDetails() else onBack()
                }
            )
            Spacer(Modifier.width(12.dp))
            Text("FreeToGame", style = MaterialTheme.typography.headlineSmall)
        }

        Spacer(Modifier.height(12.dp))

        when {
            error != null -> Text("Error: $error", color = MaterialTheme.colorScheme.error)
            selectedDetails != null -> GameDetailsView(selectedDetails!!)
            games.isEmpty() -> Text("Loading...")
            else -> LazyColumn {
                items(games) { game ->
                    GameRow(game) { vm.openGameDetails(game.id) }
                }
            }
        }
    }
}

@Composable
fun GameRow(game: Game, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(4.dp)
    ) {
        Row(Modifier.padding(12.dp)) {
            AsyncImage(
                model = game.thumbnail,
                contentDescription = game.title,
                modifier = Modifier.size(90.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(game.title, style = MaterialTheme.typography.titleMedium)
                Text("Genre: ${game.genre}")
                Text("Platform: ${game.platform}")
            }
        }
    }
}

@Composable
fun GameDetailsView(details: Description_of_Game) {

    key(details.id) {

        val context = LocalContext.current
        val db = remember { AppDatabase.getDatabase(context) }
        val reviewDao = remember { db.reviewDao() }
        val scope = rememberCoroutineScope()

        var rating by remember(details.id) { mutableStateOf(0) }
        var comment by remember(details.id) { mutableStateOf("") }

        val reviews = produceState<List<Review>>(emptyList(), details.id) {
            reviewDao.observeReviews(details.id).collectLatest { value = it }
        }.value

        LazyColumn(Modifier.padding(12.dp)) {

            item {
                Text(
                    details.title,
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            item {
                AsyncImage(
                    model = details.thumbnail,
                    contentDescription = details.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }

            item { Spacer(Modifier.height(12.dp)) }
            item { Text("Genre: ${details.genre}") }
            item { Text("Platform: ${details.platform}") }
            details.publisher?.let { item { Text("Publisher: $it") } }
            details.developer?.let { item { Text("Developer: $it") } }
            details.release_date?.let { item { Text("Release Date: $it") } }

            details.description?.let { desc ->
                item {
                    Spacer(Modifier.height(12.dp))
                    Text(desc, style = MaterialTheme.typography.bodyMedium)
                }
            }

            item { Spacer(Modifier.height(12.dp)) }

            item {
                Row {
                    (1..5).forEach {
                        Icon(
                            imageVector = if (it <= rating)
                                Icons.Filled.Star
                            else
                                Icons.Outlined.Star,
                            contentDescription = null,
                            modifier = Modifier.clickable { rating = it }
                        )
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text("Comment") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Button(
                    onClick = {
                        scope.launch {
                            reviewDao.insert(
                                Review(
                                    gameId = details.id,
                                    rating = rating,
                                    comment = comment
                                )
                            )
                            rating = 0
                            comment = ""
                        }
                    }
                ) {
                    Text("Submit")
                }
            }

            items(reviews) {
                Text("★ ${it.rating}: ${it.comment}")
            }
        }
    }
}
