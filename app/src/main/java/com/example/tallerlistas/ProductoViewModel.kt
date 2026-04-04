package com.example.tallerlistas

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class ProductoUiState(
    val productos: List<Producto>  = emptyList(),
    val registroExitoso: Boolean   = false,
    val errorNombre: String?       = null,
    val errorPrecio: String?       = null,
    val errorTipo: String?         = null
)


class ProductoViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProductoUiState())

    val uiState: StateFlow<ProductoUiState> = _uiState.asStateFlow()

    private var nextId = 1


    fun registrarProducto(nombre: String, precio: String, tipo: TipoProducto?) {
        val errorNombre = if (nombre.isBlank()) "El nombre no puede estar vacío" else null
        val precioDouble = precio.toDoubleOrNull()
        val errorPrecio  = when {
            precio.isBlank()             -> "El precio no puede estar vacío"
            precioDouble == null         -> "Ingresa un número válido"
            precioDouble <= 0            -> "El precio debe ser mayor a 0"
            else                         -> null
        }
        val errorTipo = if (tipo == null) "Selecciona un tipo de producto" else null

        if (errorNombre != null || errorPrecio != null || errorTipo != null) {
            _uiState.update { current ->
                current.copy(
                    errorNombre     = errorNombre,
                    errorPrecio     = errorPrecio,
                    errorTipo       = errorTipo,
                    registroExitoso = false
                )
            }
            return
        }

        val nuevoProducto = Producto(
            id     = nextId++,
            nombre = nombre.trim(),
            precio = precioDouble!!,
            tipo   = tipo!!
        )

        _uiState.update { current ->
            current.copy(
                productos       = current.productos + nuevoProducto,
                registroExitoso = true,
                errorNombre     = null,
                errorPrecio     = null,
                errorTipo       = null
            )
        }
    }


    fun consumirRegistroExitoso() {
        _uiState.update { it.copy(registroExitoso = false) }
    }


    fun limpiarErrorNombre()  = _uiState.update { it.copy(errorNombre = null) }
    fun limpiarErrorPrecio()  = _uiState.update { it.copy(errorPrecio = null) }
    fun limpiarErrorTipo()    = _uiState.update { it.copy(errorTipo   = null) }
}
