package com.example.bromoindah.ui.screen.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Landscape
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bromoindah.ui.viewmodel.AuthViewModel

// Nature-themed gradient colors (shared palette with LoginScreen)
private val GradientDarkGreen = Color(0xFF1B5E20)
private val GradientMediumGreen = Color(0xFF2E7D32)
private val GradientLightGreen = Color(0xFF43A047)
private val AccentGreen = Color(0xFF66BB6A)
private val SurfaceWhite = Color(0xFFFAFFF8)
private val ErrorRed = Color(0xFFD32F2F)

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    authViewModel: AuthViewModel,
) {
    val uiState by authViewModel.uiState.collectAsState()

    var fullName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var formVisible by remember { mutableStateOf(false) }
    var passwordMismatchError by rememberSaveable { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val confirmPasswordFocusRequester = remember { FocusRequester() }

    // Trigger entrance animation
    LaunchedEffect(Unit) {
        formVisible = true
    }

    // Navigate on successful registration
    LaunchedEffect(uiState.isLoggedIn) {
        if (uiState.isLoggedIn) {
            onRegisterSuccess()
        }
    }

    // Validate password match on change
    LaunchedEffect(password, confirmPassword) {
        passwordMismatchError = confirmPassword.isNotEmpty() && password != confirmPassword
    }

    val isFormValid = fullName.isNotBlank()
            && email.isNotBlank()
            && password.isNotBlank()
            && confirmPassword.isNotBlank()
            && !passwordMismatchError

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        GradientDarkGreen,
                        GradientMediumGreen,
                        GradientLightGreen,
                    )
                )
            )
            .imePadding(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Spacer(modifier = Modifier.height(36.dp))

            // App Logo & Title
            Icon(
                imageVector = Icons.Outlined.Landscape,
                contentDescription = "Logo Bromo",
                modifier = Modifier.size(56.dp),
                tint = Color.White,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "BromoInDah",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                ),
                color = Color.White,
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Form Card
            AnimatedVisibility(
                visible = formVisible,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 3 }),
                exit = fadeOut(),
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = SurfaceWhite,
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "Buat Akun Baru",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                            ),
                            color = GradientDarkGreen,
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Full Name Field
                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            label = { Text("Nama Lengkap") },
                            placeholder = { Text("Masukkan nama lengkap") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Person,
                                    contentDescription = "Ikon Nama",
                                    tint = GradientMediumGreen,
                                )
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next,
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { emailFocusRequester.requestFocus() },
                            ),
                            shape = RoundedCornerShape(16.dp),
                            colors = outlinedFieldColors(),
                            modifier = Modifier.fillMaxWidth(),
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Email Field
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            placeholder = { Text("contoh@email.com") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Email,
                                    contentDescription = "Ikon Email",
                                    tint = GradientMediumGreen,
                                )
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next,
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { passwordFocusRequester.requestFocus() },
                            ),
                            shape = RoundedCornerShape(16.dp),
                            colors = outlinedFieldColors(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(emailFocusRequester),
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Password Field
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Kata Sandi") },
                            placeholder = { Text("Buat kata sandi") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Lock,
                                    contentDescription = "Ikon Kata Sandi",
                                    tint = GradientMediumGreen,
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible) {
                                            Icons.Outlined.VisibilityOff
                                        } else {
                                            Icons.Outlined.Visibility
                                        },
                                        contentDescription = if (passwordVisible) {
                                            "Sembunyikan kata sandi"
                                        } else {
                                            "Tampilkan kata sandi"
                                        },
                                        tint = GradientMediumGreen.copy(alpha = 0.7f),
                                    )
                                }
                            },
                            singleLine = true,
                            visualTransformation = if (passwordVisible) {
                                VisualTransformation.None
                            } else {
                                PasswordVisualTransformation()
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Next,
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { confirmPasswordFocusRequester.requestFocus() },
                            ),
                            shape = RoundedCornerShape(16.dp),
                            colors = outlinedFieldColors(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(passwordFocusRequester),
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Confirm Password Field
                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = { Text("Konfirmasi Kata Sandi") },
                            placeholder = { Text("Ulangi kata sandi") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Lock,
                                    contentDescription = "Ikon Konfirmasi Kata Sandi",
                                    tint = GradientMediumGreen,
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                    Icon(
                                        imageVector = if (confirmPasswordVisible) {
                                            Icons.Outlined.VisibilityOff
                                        } else {
                                            Icons.Outlined.Visibility
                                        },
                                        contentDescription = if (confirmPasswordVisible) {
                                            "Sembunyikan kata sandi"
                                        } else {
                                            "Tampilkan kata sandi"
                                        },
                                        tint = GradientMediumGreen.copy(alpha = 0.7f),
                                    )
                                }
                            },
                            singleLine = true,
                            visualTransformation = if (confirmPasswordVisible) {
                                VisualTransformation.None
                            } else {
                                PasswordVisualTransformation()
                            },
                            isError = passwordMismatchError,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done,
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                    if (isFormValid) {
                                        authViewModel.register(
                                            email = email.trim(),
                                            password = password,
                                            fullName = fullName.trim(),
                                        )
                                    }
                                },
                            ),
                            shape = RoundedCornerShape(16.dp),
                            colors = outlinedFieldColors(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(confirmPasswordFocusRequester),
                        )

                        // Password mismatch error
                        AnimatedVisibility(
                            visible = passwordMismatchError,
                            enter = fadeIn() + slideInVertically(),
                            exit = fadeOut(),
                        ) {
                            Text(
                                text = "Kata sandi tidak cocok",
                                color = ErrorRed,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 8.dp, top = 4.dp),
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Server Error Message
                        AnimatedVisibility(
                            visible = uiState.error != null,
                            enter = fadeIn() + slideInVertically(),
                            exit = fadeOut(),
                        ) {
                            Text(
                                text = uiState.error.orEmpty(),
                                color = ErrorRed,
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Register Button
                        Button(
                            onClick = {
                                focusManager.clearFocus()
                                authViewModel.register(
                                    email = email.trim(),
                                    password = password,
                                    fullName = fullName.trim(),
                                )
                            },
                            enabled = !uiState.isLoading && isFormValid,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GradientMediumGreen,
                                disabledContainerColor = AccentGreen.copy(alpha = 0.4f),
                            ),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 4.dp,
                                pressedElevation = 8.dp,
                            ),
                        ) {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color.White,
                                    strokeWidth = 2.5.dp,
                                )
                            } else {
                                Text(
                                    text = "Daftar",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                    ),
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Login Navigation
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                text = "Sudah punya akun?",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray,
                            )
                            TextButton(onClick = onNavigateToLogin) {
                                Text(
                                    text = "Masuk",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                    ),
                                    color = GradientMediumGreen,
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(36.dp))
        }
    }
}

/**
 * Shared outlined text field colors for the green nature theme.
 */
@Composable
private fun outlinedFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = GradientMediumGreen,
    unfocusedBorderColor = AccentGreen.copy(alpha = 0.5f),
    cursorColor = GradientMediumGreen,
    focusedLabelColor = GradientMediumGreen,
    errorBorderColor = ErrorRed,
    errorLabelColor = ErrorRed,
    errorCursorColor = ErrorRed,
)
