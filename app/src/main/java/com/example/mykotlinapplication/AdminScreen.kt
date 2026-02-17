package com.example.mykotlinapplication

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.mykotlinapplication.data.User
import com.example.mykotlinapplication.data.UserDao
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    userDao: UserDao,
    onBack: () -> Unit,
    onSignOut: () -> Unit
) {
    val scope = rememberCoroutineScope()

    var users by remember { mutableStateOf<List<User>>(emptyList()) }

    // ✅ Add-user form state
    var newUsername by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }

    // ✅ Snackbar
    val snackbarHostState = remember { SnackbarHostState() }

    fun refresh() {
        scope.launch {
            users = userDao.getAll()
        }
    }

    LaunchedEffect(Unit) { refresh() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Panel") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Back") } },
                actions = { TextButton(onClick = onSignOut) { Text("Sign Out") } }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {

            // ✅ Add User Section
            Text("Add User", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = newUsername,
                onValueChange = { newUsername = it },
                label = { Text("Username") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(10.dp))

            val canAdd = newUsername.trim().isNotEmpty() && newPassword.trim().isNotEmpty()

            Button(
                onClick = {
                    scope.launch {
                        val u = newUsername.trim()
                        val p = newPassword.trim()

                        // ✅ If you updated DAO to return Long:
                        val result = userDao.insert(User(username = u, password = p))

                        if (result == -1L) {
                            snackbarHostState.showSnackbar("User \"$u\" already exists (ignored).")
                        } else {
                            snackbarHostState.showSnackbar("Added user \"$u\".")
                            newUsername = ""
                            newPassword = ""
                            refresh()
                        }

                        // If you DID NOT change DAO and still have insert(user: User) with Unit return,
                        // then replace the block above with:
                        //
                        // userDao.insert(User(username = u, password = p))
                        // snackbarHostState.showSnackbar("Added user \"$u\".")
                        // newUsername = ""
                        // newPassword = ""
                        // refresh()
                    }
                },
                enabled = canAdd,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Add User")
            }

            Spacer(Modifier.height(18.dp))
            Divider()
            Spacer(Modifier.height(12.dp))

            // ✅ Existing users list
            if (users.isEmpty()) {
                Text("No users found.")
            } else {
                Text("Total Users: ${users.size}")
                Spacer(Modifier.height(12.dp))

                LazyColumn {
                    items(users, key = { it.uid }) { user ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("ID: ${user.uid}")
                                    Text("Username: ${user.username}")
                                    Text("Password: ${user.password}")
                                }

                                OutlinedButton(
                                    onClick = {
                                        scope.launch {
                                            userDao.delete(user)
                                            refresh()
                                            snackbarHostState.showSnackbar("Deleted \"${user.username}\".")
                                        }
                                    }
                                ) { Text("Delete") }
                            }
                        }
                    }
                }
            }
        }
    }
}
