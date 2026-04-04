package com.example.tallerlistas

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val Purple80  = Color(0xFF6C63FF)
val Pink80    = Color(0xFFFF6B9D)
val DarkBg    = Color(0xFF0F0F1A)
val CardDark  = Color(0xFF1A1A2E)
val CardDark2 = Color(0xFF16213E)
val TextLight = Color(0xFFF0F0FF)
val TextMuted = Color(0xFF9090BB)

@Composable
fun HomeScreen(
    viewModel: ProductoViewModel,
    onNavigateRegister: () -> Unit,
    onNavigateList: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val totalProductos = uiState.productos.size

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(DarkBg, Color(0xFF0D0D2B), CardDark2)))
    ) {
        Box(
            modifier = Modifier
                .size(320.dp)
                .align(Alignment.TopEnd)
                .offset(x = 80.dp, y = (-60).dp)
                .clip(CircleShape)
                .background(Brush.radialGradient(listOf(Purple80.copy(0.15f), Color.Transparent)))
        )
        Box(
            modifier = Modifier
                .size(240.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-60).dp, y = 60.dp)
                .clip(CircleShape)
                .background(Brush.radialGradient(listOf(Pink80.copy(0.12f), Color.Transparent)))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Brush.linearGradient(listOf(Purple80, Pink80))),
                contentAlignment = Alignment.Center
            ) {
                Text("🛍️", fontSize = 42.sp)
            }

            Spacer(Modifier.height(28.dp))

            Text(
                "ShopTrack",
                fontSize = 38.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight,
                letterSpacing = (-1).sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Gestiona tus productos\nde forma sencilla",
                fontSize = 15.sp,
                color = TextMuted,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(Modifier.height(52.dp))

            HomeActionCard(
                emoji    = "✏️",
                title    = "Registrar Producto",
                subtitle = "Agrega un nuevo artículo",
                gradient = Brush.linearGradient(listOf(Purple80, Color(0xFF9B8FF5))),
                icon     = Icons.Default.Add,
                onClick  = onNavigateRegister
            )

            Spacer(Modifier.height(16.dp))

            HomeActionCard(
                emoji    = "📦",
                title    = "Ver Productos",
                subtitle = "Consulta tu inventario ($totalProductos)",
                gradient = Brush.linearGradient(listOf(Pink80, Color(0xFFFF9BB5))),
                icon     = Icons.Default.List,
                onClick  = onNavigateList
            )

            Spacer(Modifier.height(48.dp))

            Text(
                "4 categorías disponibles  •  v2.0 ViewModel",
                fontSize = 12.sp,
                color = TextMuted.copy(0.5f)
            )
        }
    }
}

@Composable
fun HomeActionCard(
    emoji: String,
    title: String,
    subtitle: String,
    gradient: Brush,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(88.dp)
            .shadow(12.dp, RoundedCornerShape(20.dp), ambientColor = Purple80.copy(0.3f)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardDark)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(gradient),
                contentAlignment = Alignment.Center
            ) {
                Text(emoji, fontSize = 24.sp)
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextLight)
                Text(subtitle, fontSize = 13.sp, color = TextMuted)
            }
            Icon(icon, null, tint = TextMuted.copy(0.5f), modifier = Modifier.size(20.dp))
        }
    }
}