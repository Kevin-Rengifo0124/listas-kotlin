package com.example.tallerlistas

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    viewModel: ProductoViewModel,
    onBack: () -> Unit
) {
    val uiState  by viewModel.uiState.collectAsState()
    val productos = uiState.productos

    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "CO"))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(DarkBg, Color(0xFF0D0D2B), CardDark2)))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Mis Productos",
                            fontWeight = FontWeight.Bold,
                            color = TextLight,
                            fontSize = 20.sp
                        )
                        Text(
                            "${productos.size} artículo${if (productos.size != 1) "s" else ""}",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
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
                actions = {
                    if (productos.isNotEmpty()) {
                        val tipoCount = productos.groupBy { it.tipo }
                        tipoCount.keys.take(2).forEach { tipo ->
                            Box(
                                modifier = Modifier
                                    .padding(end = 6.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(tipo.color.copy(0.2f))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    "${tipo.emoji} ${tipoCount[tipo]?.size}",
                                    fontSize = 12.sp,
                                    color = tipo.color
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )

            if (productos.isEmpty()) {
                EmptyState()
            } else {
                val total = productos.sumOf { it.precio }
                TotalSummaryBar(total = total, currencyFormat = currencyFormat)

                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    itemsIndexed(
                        items = productos,
                        key   = { _, p -> p.id }          // key estable → animaciones suaves
                    ) { index, producto ->
                        AnimatedVisibility(
                            visible = true,
                            enter   = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn()
                        ) {
                            ProductCard(
                                producto       = producto,
                                index          = index,
                                currencyFormat = currencyFormat
                            )
                        }
                    }
                    item { Spacer(Modifier.height(24.dp)) }
                }
            }
        }
    }
}


@Composable
fun TotalSummaryBar(total: Double, currencyFormat: NumberFormat) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        shape  = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(listOf(Purple80.copy(0.2f), Pink80.copy(0.2f))),
                    RoundedCornerShape(14.dp)
                )
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("💰 Valor total del inventario", fontSize = 13.sp, color = TextMuted)
                Text(
                    currencyFormat.format(total),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextLight
                )
            }
        }
    }
}


@Composable
fun ProductCard(
    producto: Producto,
    index: Int,
    currencyFormat: NumberFormat
) {
    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(20.dp),
        colors    = CardDefaults.cardColors(containerColor = CardDark),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.radialGradient(
                            listOf(
                                producto.tipo.color.copy(0.4f),
                                producto.tipo.color.copy(0.1f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(producto.tipo.emoji, fontSize = 30.sp)
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = producto.nombre,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextLight,
                    maxLines = 2
                )
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(producto.tipo.color.copy(0.15f))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = producto.tipo.label,
                            fontSize = 11.sp,
                            color = producto.tipo.color,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Text("# ${index + 1}", fontSize = 11.sp, color = TextMuted.copy(0.5f))
                }
            }

            Spacer(Modifier.width(12.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = currencyFormat.format(producto.precio),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextLight
                )
                Text("COP", fontSize = 10.sp, color = TextMuted.copy(0.5f))
            }
        }
    }
}


@Composable
fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("📭", fontSize = 72.sp)
        Spacer(Modifier.height(20.dp))
        Text(
            "No hay productos aún",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TextLight
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Regresa al inicio y registra\ntu primer artículo",
            fontSize = 14.sp,
            color = TextMuted,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )
    }
}
