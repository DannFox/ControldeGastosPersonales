# Control de Gastos Personales

Esta es una aplicación nativa de Android para gestionar finanzas personales. Permite a los usuarios registrar sus ingresos y gastos, administrarlos en diferentes cuentas y categorizarlos para un mejor seguimiento.

## ✨ Características

*   **Dashboard Principal:** Ofrece un resumen visual del balance total, ingresos y gastos.
*   **Gráficos Interactivos:** Muestra gráficos de pastel para visualizar la distribución de ingresos y gastos por categoría.
*   **Gestión de Cuentas:** Permite crear, ver, editar y eliminar múltiples cuentas (ej: banco, efectivo, tarjeta de crédito).
*   **Gestión de Categorías:** Administra las categorías para clasificar los movimientos.
*   **Registro de Movimientos:** Añade nuevos ingresos y gastos de forma sencilla.
*   **Historial de Movimientos:** Visualiza una lista detallada de todas las transacciones.
*   **Recordatorios Diarios:** Envía una notificación diaria para recordar al usuario que registre sus movimientos.

## 🛠️ Tecnologías Utilizadas

*   **Lenguaje:** [Kotlin](https://kotlinlang.org/)
*   **Arquitectura:** Diseño MVVM (Model-View-ViewModel) implícito.
*   **Librerías Principales:**
    *   **Retrofit & OkHttp:** Para la comunicación con la API REST.
    *   **Gson:** Para la serialización/deserialización de datos JSON.
    *   **MPAndroidChart:** Para la creación de los gráficos de pastel.
    *   **AndroidX Libraries:** (AppCompat, RecyclerView, Material Components).
    *   **Coroutines:** Para la gestión de tareas asíncronas.

## ⚙️ Configuración del Backend

Esta aplicación requiere un backend para funcionar. La URL base está configurada en el archivo `APIClient.kt` para apuntar a un servidor local.

```kotlin
private const val base_url = "http://10.0.2.2:5259/api/"
```

*   La IP `10.0.2.2` es una dirección especial que permite al emulador de Android conectarse al `localhost` de la máquina anfitriona.
*   Asegúrate de tener el servicio de la API corriendo en `http://localhost:5259` en tu máquina para que la aplicación pueda conectarse correctamente.

## 🚀 Cómo Empezar

1.  **Clona el repositorio:**
    ```bash
    git clone <URL_DEL_REPOSITORIO>
    ```
2.  **Abre el proyecto** en Android Studio.
3.  **Ejecuta el backend** necesario para la aplicación.
4.  **Construye y ejecuta la app** en un emulador o dispositivo físico.

