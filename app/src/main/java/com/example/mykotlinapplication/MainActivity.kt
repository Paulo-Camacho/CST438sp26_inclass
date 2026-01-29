package com.example.mykotlinapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.mykotlinapplication.ui.theme.MyKotlinApplicationTheme
import androidx.compose.foundation.clickable
import androidx.compose.ui.Alignment


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
                            onBack = { showGames = false })
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

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun GamesScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    vm: GamesViewModel = viewModel()
) {
    val games by vm.games.collectAsState()
    val error by vm.error.collectAsState()

    var sortBy by remember { mutableStateOf("alphabetical") }
    var category by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        //Text("FreeToGame", style = MaterialTheme.typography.headlineSmall)

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "← Back",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(end = 12.dp)
                    .clickable { onBack() }
            )

            Text(
                text = "FreeToGame",
                style = MaterialTheme.typography.headlineSmall
            )
        }



        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Updated to pass modifiers to handle weight/width if needed
            CustomDropdownMenu(
                selected = sortBy,
                onSelectedChange = {
                    sortBy = it
                    vm.sortBy = it
                },
                label = "Sort By",
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(12.dp))
            CustomDropdownMenu(
                selected = category,
                onSelectedChange = {
                    category = it
                    vm.category = it
                    vm.fetchGames()
                },
                label = "Filter by Genre",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        when {
            error != null -> {
                Text("Error: $error", color = MaterialTheme.colorScheme.error)
            }
            games.isEmpty() -> {
                Text("Loading...")
            }
            else -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(games) { game ->
                        GameRow(game)
                    }
                }
            }
        }

        LaunchedEffect(sortBy) {
            vm.sortBy = sortBy
            vm.fetchGames()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdownMenu(
    selected: String,
    onSelectedChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            val items = if (label == "Sort By") {
                listOf("alphabetical", "release-date", "popularity", "relevance")
            } else {
                listOf(
                    "mmorpg", "shooter", "strategy", "moba", "racing",
                    "sports", "social", "sandbox", "open-world", "survival",
                    "pvp", "pve", "pixel", "voxel", "zombie",
                    "turn-based", "first-person", "third-person", "top-down", "tank",
                    "space", "sailing", "side-scroller", "superhero", "permadeath",
                    "card", "battle-royale", "mmo", "mmofps", "mmotps",
                    "3d", "2d", "anime", "fantasy", "sci-fi",
                    "fighting", "action-rpg", "action", "military", "martial-arts",
                    "flight", "low-spec", "tower-defense", "horror", "mmorts"
                )            }

            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onSelectedChange(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun GameRow(game: Game) {
    Card(modifier = Modifier.fillMaxWidth()) {
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