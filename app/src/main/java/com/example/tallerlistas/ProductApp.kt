package com.example.tallerlistas

import androidx.compose.ui.graphics.Color


enum class TipoProducto(
    val label: String,
    val emoji: String,
    val color: Color
) {
    TECNOLOGIA("Tecnología",  "💻", Color(0xFF6C63FF)),
    ROPA      ("Ropa",        "👗", Color(0xFFFF6B9D)),
    ALIMENTOS ("Alimentos",   "🍎", Color(0xFF4CAF50)),
    HOGAR     ("Hogar",       "🏠", Color(0xFFFF9800))
}

data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val tipo: TipoProducto
)

object Routes {
    const val HOME     = "home"
    const val REGISTER = "register"
    const val LIST     = "list"
    const val CAMERA   = "camera"
}