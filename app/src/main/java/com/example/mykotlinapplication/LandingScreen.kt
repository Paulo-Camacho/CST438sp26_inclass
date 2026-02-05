package com.example.mykotlinapplication

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close

@Composable
fun LandingScreen(
    randomGame: Game?,
    onSearchGames: () -> Unit,
    onRandomRequested: () -> Unit,
    onClearRandom: () -> Unit,
    onSignOut: () -> Unit,
    onGameClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "FreeToGame",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Random suggestion area: shown only when randomGame is present
        if (randomGame != null) {
            RandomGameCard(game = randomGame, onClear = onClearRandom, onClick = onGameClick)
            Spacer(modifier = Modifier.height(16.dp))
        }

        // This spacer pushes the button block to the bottom of the screen
        Spacer(modifier = Modifier.weight(1f))

        // Buttons (Search / Random / Sign out)
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onSearchGames,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Search Games")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onRandomRequested,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Random Game")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onSignOut,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Sign Out")
            }

            //Spacer to float the buttons slightly above the bottom edge.
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun RandomGameCard(game: Game, onClear: () -> Unit, onClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onClick(game.id) } // entire card is tappable to open details
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = game.thumbnail,
                contentDescription = game.title,
                modifier = Modifier.size(90.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(game.title, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Genre: ${game.genre}", style = MaterialTheme.typography.bodySmall)
                Text("Platform: ${game.platform}", style = MaterialTheme.typography.bodySmall)
            }

            IconButton(onClick = onClear) {
                Icon(Icons.Default.Close, contentDescription = "Dismiss suggestion")
            }
        }
    }
}