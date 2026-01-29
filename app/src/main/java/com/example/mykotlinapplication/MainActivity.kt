package com.example.mykotlinapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.mykotlinapplication.ui.theme.MyKotlinApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyKotlinApplicationTheme {
                var showGames by rememberSaveable { mutableStateOf(false) }
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (showGames) {
                        GamesScreen(
                            modifier = Modifier.padding(innerPadding),
                            onBack = { showGames = false }
                        )
                    } else {
                        LandingScreen(
                            modifier = Modifier.padding(innerPadding),
                            onSearchGames = { showGames = true },
                            onSignOut = {
                                // TODO: When sign-in screen exists, navigate there.
                                // For now you can just stay here or set showGames = false.
                                showGames = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GamesScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    vm: GamesViewModel = viewModel()
) {
    val games by vm.games.collectAsState()
    val error by vm.error.collectAsState()

    val selectedDetails by vm.selectedGameDetails.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "← Back",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(end = 12.dp)
                    .clickable {
                        // If currently viewing details, go back to list first. Otherwise, go back to landing screen.
                        if (selectedDetails != null) vm.closeGameDetails() else onBack()
                    }
            )

            Text(
                text = "FreeToGame",
                style = MaterialTheme.typography.headlineSmall
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        when {
            error != null -> {
                Text("Error: $error", color = MaterialTheme.colorScheme.error)
            }

            // NEW: If a game is selected, show details instead of the list
            selectedDetails != null -> {
                GameDetailsView(details = selectedDetails!!)
            }

            games.isEmpty() -> {
                Text("Loading...")
            }

            else -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(games) { game ->
                        GameRow(
                            game = game,
                            onClick = { vm.openGameDetails(game.id) } // NEW
                        )
                    }
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
            .clickable { onClick() } // NEW
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            AsyncImage(
                model = game.thumbnail,
                contentDescription = game.title,
                modifier = Modifier.size(90.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(game.title, style = MaterialTheme.typography.titleMedium)
                Text("Genre: ${game.genre}")
                Text("Platform: ${game.platform}")
            }
        }
    }
}

@Composable
fun GameDetailsView(details: GameDetails) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = details.title,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(12.dp))

        AsyncImage(
            model = details.thumbnail,
            contentDescription = details.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("Genre: ${details.genre}")
        Text("Platform: ${details.platform}")
        details.publisher?.let { Text("Publisher: $it") }
        details.developer?.let { Text("Developer: $it") }
        details.release_date?.let { Text("Release Date: $it") }

        Spacer(modifier = Modifier.height(12.dp))

        details.description?.let { desc ->
            Text(desc, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
