package com.example.mykotlinapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mykotlinapplication.ui.theme.MyKotlinApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyKotlinApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainLandingScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainLandingScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally

    ) {

        // Temporary app title
        Text(
            text = "Choose My Next Game",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.weight(6f))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {

            Button(
                onClick = {
                    // TODO: Navigate to next page
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Search Games")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // TODO: Sign out
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Sign Out")
            }
        }

        // Push buttons upward equally (keeps them centered)
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MyKotlinApplicationTheme {
        MainLandingScreen()
    }
}