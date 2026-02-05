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
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.mykotlinapplication.data.AppDatabase
import com.example.mykotlinapplication.data.Review
import com.example.mykotlinapplication.data.User
import com.example.mykotlinapplication.ui.theme.MyKotlinApplicationTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = AppDatabase.getDatabase(this)
        val userDao = db.userDao()

        lifecycleScope.launch {
            userDao.insert(User(username = "admin", password = "password"))
        }

        setContent {
            MyKotlinApplicationTheme {
                val vm: GamesViewModel = viewModel()
                val random by vm.randomGame.collectAsState()
                var showGames by rememberSaveable { mutableStateOf(false) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (showGames) {
                        GamesScreen(
                            modifier = Modifier.padding(innerPadding),
                            onBack = { showGames = false },
                            vm = vm
                        )
                    } else {
                        LandingScreen(
                            modifier = Modifier.padding(innerPadding),
                            randomGame = random,
                            onSearchGames = { showGames = true },
                            onRandomRequested = { vm.pickRandomGame() },
                            onClearRandom = { vm.clearRandom() },
                            onSignOut = { /* TODO: sign out */ },
                            onGameClick = { id ->
                                vm.openGameDetails(id)
                                showGames = true
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

    val selectedDetails by vm.selectedDescriptionofGame.collectAsState()
    var sortBy by remember { mutableStateOf("alphabetical") }
    var category by remember { mutableStateOf("") }

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
                        if (selectedDetails != null) vm.closeGameDetails() else onBack()
                    }
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
                Text("SOMETHING WENT WRONG! : $error", color = MaterialTheme.colorScheme.error)
            }
            selectedDetails != null -> {
                GameDetailsView(details = selectedDetails!!)
            }
            games.isEmpty() -> {
                Text("hold your horses...")
            }
            else -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(games) { game ->
                        GameRow(
                            game = game,
                            onClick = { vm.openGameDetails(game.id) }
                        )
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
                .fillMaxWidth()
                .menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            val itemsList = if (label == "Sort By") {
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
                )
            }

            itemsList.forEach { item ->
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
fun GameRow(game: Game, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
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

/**
 * FIXED:
 * - Uses ONE LazyColumn (no nested LazyColumn)
 * - Adds missing imports via Material3.*
 */
@Composable
fun GameDetailsView(details: Description_of_Game) {
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val reviewDao = remember { db.reviewDao() }
    val scope = rememberCoroutineScope()

    var rating by remember { mutableStateOf(0) }
    var comment by remember { mutableStateOf("") }
    var submitError by remember { mutableStateOf<String?>(null) }

    val reviewsState = produceState<List<Review>>(initialValue = emptyList(), key1 = details.id) {
        reviewDao.observeReviews(details.id).collectLatest { value = it }
    }

    val avgRatingState = produceState<Double?>(initialValue = null, key1 = details.id) {
        reviewDao.observeAverageRating(details.id).collectLatest { value = it }
    }

    val reviews = reviewsState.value
    val avg = avgRatingState.value

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(details.title, style = MaterialTheme.typography.headlineSmall)
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

        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Genre: ${details.genre}")
                Text("Platform: ${details.platform}")
                details.publisher?.let { Text("Publisher: $it") }
                details.developer?.let { Text("Developer: $it") }
                details.release_date?.let { Text("Release Date: $it") }
            }
        }

        details.description?.let { desc ->
            item {
                Text(desc, style = MaterialTheme.typography.bodyMedium)
            }
        }

        item {
            Divider()
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Ratings & Comments", style = MaterialTheme.typography.titleMedium)

                Text(
                    text = if (avg != null) "Average rating: ${"%.1f".format(avg)} / 5" else "Average rating: —"
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Your rating: ", modifier = Modifier.padding(end = 8.dp))
                    (1..5).forEach { i ->
                        Icon(
                            imageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                            contentDescription = "Star $i",
                            modifier = Modifier
                                .size(28.dp)
                                .clickable { rating = i }
                                .padding(end = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(if (rating == 0) "None" else "$rating/5")
                }

                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text("Add a comment (optional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                submitError?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }

                Button(
                    onClick = {
                        submitError = null
                        val trimmed = comment.trim()
                        val hasRating = rating in 1..5
                        val hasComment = trimmed.isNotEmpty()

                        if (!hasRating && !hasComment) {
                            submitError = "Please add a rating or a comment."
                            return@Button
                        }

                        scope.launch {
                            reviewDao.insert(
                                Review(
                                    gameId = details.id,
                                    rating = if (hasRating) rating else 0,
                                    comment = trimmed
                                )
                            )
                            rating = 0
                            comment = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Submit")
                }
            }
        }

        item {
            Text("Recent", style = MaterialTheme.typography.titleSmall)
        }

        if (reviews.isEmpty()) {
            item { Text("No ratings/comments yet.") }
        } else {
            items(reviews) { r ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        if (r.rating > 0) {
                            Text("Rating: ${r.rating}/5", style = MaterialTheme.typography.titleSmall)
                        }
                        if (r.comment.isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(r.comment)
                        }
                    }
                }
            }
        }
    }
}
