package com.example.p_9

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.p_9.ui.theme.Mgreen
import com.example.p_9.ui.theme.P9Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            P9Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    mainScreen()
                }
            }
        }
    }
}

@Composable
fun mainScreen() {
    var isLoggedOut by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val context = LocalContext.current
    val storedEmail by EmailDataStore.getEmail(context).collectAsState(initial = null)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isLoggedOut || storedEmail == null){
                formInputs(
                    onEmailEntered = { newEmail -> email = newEmail },
                    onPasswordEntered = { newPassword -> password = newPassword }
                )
                submit(email) { newValue -> isLoggedOut = newValue }
            } else {
                Text(text = "Welcome $storedEmail", color = Color.White)
                logOut(storedEmail!!) { newValue -> isLoggedOut = newValue }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun formInputs(onEmailEntered: (String) -> Unit,
               onPasswordEntered: (String) -> Unit,) {
    Column {
        Text(text = "Email", color = Color.White)
        var email by remember { mutableStateOf("") }
        Box(
            modifier = Modifier
                .padding(5.dp, 10.dp)
                .background(Color.Black)
                .border(width = 1.dp, color = Mgreen, shape = CircleShape) // Set the border color and width
        ){

            TextField(value = email, onValueChange = {
                email = it
                onEmailEntered(it) // Invoke the lambda with the updated name
            }, modifier = Modifier.padding(0.dp),
                colors = TextFieldDefaults.textFieldColors(containerColor = Color.Black, focusedTextColor = Mgreen,unfocusedTextColor = Color.White,focusedIndicatorColor = Color.Transparent, // Remove the focused indicator
                    unfocusedIndicatorColor = Color.Transparent))
        }
        Text(text = "Password", color = Color.White)
        var password by remember { mutableStateOf("") }
        Box(
            modifier = Modifier
                .padding(5.dp, 10.dp)
                .background(Color.Black)
                .border(width = 1.dp, color = Mgreen, shape = CircleShape), // Set the border color and width
        ){

            TextField(value = password, onValueChange = {
                password = it
                onPasswordEntered(it)
            }, modifier = Modifier.padding(0.dp),
                colors = TextFieldDefaults.textFieldColors(containerColor = Color.Black,focusedTextColor = Mgreen, unfocusedTextColor = Color.White,focusedIndicatorColor = Color.Transparent, // Remove the focused indicator
                    unfocusedIndicatorColor = Color.Transparent),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
        }
    }
}
@Composable
fun submit(
    email: String,
    isLoggedOut: (Boolean) -> Unit
) {
    val context = LocalContext.current
    Button(
        onClick = {
            // Store the email using CoroutineScope
            CoroutineScope(Dispatchers.IO).launch {
                EmailDataStore.setEmail(context, email)
            }
            isLoggedOut(false)
        },
        colors = ButtonDefaults.buttonColors(Mgreen)
    ) {
        Text(text = "Submit", color = Color.Black, modifier = Modifier.padding(10.dp, 0.dp))
    }
}

@Composable
fun logOut(
    email: String,
    isLoggedOut: (Boolean) -> Unit
) {
    val context = LocalContext.current
    Button(
        onClick = {
            // Delete stored email
            CoroutineScope(Dispatchers.IO).launch {
                EmailDataStore.setEmail(context , "")
            }
            isLoggedOut(true)
        },
        colors = ButtonDefaults.buttonColors(Mgreen)
    ) {
        Text(text = "Log Out", color = Color.Black, modifier = Modifier.padding(10.dp, 0.dp))
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    P9Theme {
        mainScreen()
    }
}