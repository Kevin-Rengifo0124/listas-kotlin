package com.example.tallerlistas

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: ProductoViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var nombre           by remember { mutableStateOf("") }
    var precio           by remember { mutableStateOf("") }
    var tipoSeleccionado by remember { mutableStateOf<TipoProducto?>(null) }

    LaunchedEffect(uiState.registroExitoso) {
        if (uiState.registroExitoso) {
            nombre = ""
            precio = ""
            tipoSeleccionado = null
            delay(2600)
            viewModel.consumirRegistroExitoso()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(DarkBg, Color(0xFF0D0D2B), CardDark2)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // ── TopBar ───────────────────────────────────────────────────────
            TopAppBar(
                title = {
                    Text(
                        "Registrar Producto",
                        fontWeight = FontWeight.Bold,
                        color = TextLight,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = TextLight
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                AnimatedVisibility(
                    visible = uiState.registroExitoso,
                    enter   = slideInVertically() + fadeIn(),
                    exit    = slideOutVertically() + fadeOut()
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B4332))
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF4CAF50)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(20.dp))
                            }
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(
                                    "¡Producto registrado!",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF81C784)
                                )
                                Text(
                                    "El artículo fue agregado correctamente.",
                                    fontSize = 12.sp,
                                    color = Color(0xFF81C784).copy(0.7f)
                                )
                            }
                        }
                    }
                }

                SectionLabel("Información del producto")

                StyledTextField(
                    value           = nombre,
                    onValueChange   = {
                        nombre = it
                        viewModel.limpiarErrorNombre()
                    },
                    label           = "Nombre del producto",
                    placeholder     = "Ej. Audífonos Sony WH-1000XM5",
                    isError         = uiState.errorNombre != null,
                    errorMsg        = uiState.errorNombre ?: ""
                )

                StyledTextField(
                    value           = precio,
                    onValueChange   = {
                        precio = it
                        viewModel.limpiarErrorPrecio()
                    },
                    label           = "Precio",
                    placeholder     = "0.00",
                    isError         = uiState.errorPrecio != null,
                    errorMsg        = uiState.errorPrecio ?: "",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    prefix          = "$ "
                )

                SectionLabel("Tipo de producto")

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TipoProducto.entries.take(2).forEach { tipo ->
                            TipoCard(
                                tipo     = tipo,
                                selected = tipoSeleccionado == tipo,
                                modifier = Modifier.weight(1f),
                                onClick  = {
                                    tipoSeleccionado = tipo
                                    viewModel.limpiarErrorTipo()
                                }
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TipoProducto.entries.drop(2).forEach { tipo ->
                            TipoCard(
                                tipo     = tipo,
                                selected = tipoSeleccionado == tipo,
                                modifier = Modifier.weight(1f),
                                onClick  = {
                                    tipoSeleccionado = tipo
                                    viewModel.limpiarErrorTipo()
                                }
                            )
                        }
                    }
                }

                AnimatedVisibility(visible = uiState.errorTipo != null) {
                    Text(
                        uiState.errorTipo ?: "",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = {
                        viewModel.registrarProducto(nombre, precio, tipoSeleccionado)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(listOf(Purple80, Pink80)),
                                RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "✓  Registrar Producto",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}


@Composable
fun SectionLabel(text: String) {
    Text(
        text = text.uppercase(),
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = TextMuted,
        letterSpacing = 1.5.sp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StyledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    isError: Boolean = false,
    errorMsg: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    prefix: String = ""
) {
    Column {
        OutlinedTextField(
            value           = value,
            onValueChange   = onValueChange,
            label           = { Text(label, color = TextMuted) },
            placeholder     = { Text(placeholder, color = TextMuted.copy(0.4f)) },
            prefix          = if (prefix.isNotEmpty()) { { Text(prefix, color = TextMuted) } } else null,
            isError         = isError,
            keyboardOptions = keyboardOptions,
            singleLine      = true,
            modifier        = Modifier.fillMaxWidth(),
            shape           = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor        = TextLight,
                unfocusedTextColor      = TextLight,
                focusedBorderColor      = Purple80,
                unfocusedBorderColor    = TextMuted.copy(0.3f),
                cursorColor             = Purple80,
                focusedContainerColor   = CardDark,
                unfocusedContainerColor = CardDark,
                errorContainerColor     = Color(0xFF2A0A0A)
            )
        )
        AnimatedVisibility(visible = isError && errorMsg.isNotEmpty()) {
            Text(
                errorMsg,
                color = MaterialTheme.colorScheme.error,
                fontSize = 11.sp,
                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
            )
        }
    }
}

@Composable
fun TipoCard(
    tipo: TipoProducto,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val borderColor = if (selected) tipo.color else Color.Transparent
    val bgColor     = if (selected) tipo.color.copy(0.15f) else CardDark

    Card(
        modifier = modifier
            .height(80.dp)
            .border(2.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape  = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(tipo.emoji, fontSize = 26.sp)
            Spacer(Modifier.height(4.dp))
            Text(
                tipo.label,
                fontSize = 12.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) tipo.color else TextMuted
            )
        }
    }
}