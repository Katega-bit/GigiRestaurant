# GigiRestaurant

## Arquitectura

La aplicación **GigiRestaurant** está diseñada siguiendo los principios de **Clean Architecture** y **MVVM**.

* **Presentation (UI)**

  * Jetpack Compose para la construcción de la interfaz.
  * Un único `Activity` con un `NavHost` para la navegación.
  * `ViewModel` (HiltViewModel) por pantalla, expone `StateFlow` para:

    * Lista de restaurantes (`restaurants`).
    * Estado de carga (`isLoading`, `isRefreshing`).
    * Favoritos (`favoriteIds`).
    * Información de ubicación (`latLon`, `streetName`).

* **Domain**

  * Modelos de dominio (`Restaurant`, `RestaurantDetail`).
  * Interfaz de repositorio (`RestaurantRepository`).
  * Casos de uso encapsulados en métodos del repositorio.

* **Data**

  * **Remote**: Retrofit + Moshi + OkHttp para consumir Geoapify API (`Places` y `Place Details`).
  * **Local**: Room para persistir restaurantes y favoritos.
  * Mappers para transformar DTOs en modelos de dominio y viceversa.
  * Inyección de dependencias con Hilt (Retrofit, Room, FusedLocation).

* **Flujos y concurrencia**

  * Coroutines + `StateFlow`/`Flow` para estados y datos reactivos.
  * `callbackFlow` (o carga inicial) para obtener ubicación con FusedLocation.
  * Pull-to-Refresh personalizado con Material3.

## Instrucciones de ejecución

1. Clona el repositorio:

   ```bash
   git clone https://github.com/tu-usuario/GigiRestaurant.git
   cd GigiRestaurant
   ```

2. Configura tu **API key** de Geoapify:

   * Regístrate en [https://www.geoapify.com](https://www.geoapify.com) y obtén tu clave.
   * Añade la propiedad en `local.properties`:

     ```properties
     GEOAPIFY_API_KEY=TU_API_KEY
     ```

3. Abre el proyecto en Android Studio (Arctic Fox o superior).

4. Sincroniza Gradle y compila:

   * Gradle debería usar la versión de AGP `8.9.2` y Kotlin `2.0.21`.

5. Ejecuta en un emulador o dispositivo real (minSdk 24).

   * Asegúrate de otorgar permisos de ubicación cuando se soliciten.

6. Pruebas:

   * Unit tests: `./gradlew testDebugUnitTest`

## Aspectos pospuestos por falta de tiempo

* **Cache offline avanzado**: Sólo se implementa almacenamiento local básico. En producción usaría estrategia de validación TTL, DataStore para configuraciones y detección de conectividad con `ConnectivityManager`.
* **Más cobertura de tests**:

  * UI tests con Compose Test Rule.
  * Tests instrumentados de integración (navegación, permisos).
* **Optimización de rendimiento**:

  * Shimmer/placeholder mientras carga imágenes.
  * LRU cache para imágenes en Coil.
* **Internacionalización y accesibilidad**:

  * Strings en `strings.xml`, soporte multi-idioma.
  * Roles de accesibilidad y descripciones para TalkBack.
* **CI/CD**:

  * Pipeline en GitHub Actions para build + tests + lint.
* **Funciones avanzadas**:

  * Mapa interactivo con Google Maps Compose y posicionamiento de marcadores.
  * Filtros por distancia, categoría o puntuación.
  * Paginación en listas grandes.
