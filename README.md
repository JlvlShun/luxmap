# LuxMap
Aplicación Android que registra mediciones de luminosidad (lux) junto a la ubicación geográfica donde fué realizada la medición.

## Descripción
**La app permite:**
- Registrar una medición que contiene un título, categoría, descripción, coordenadas (latitud y longitud) y nivel de luz en lux, utilizando el sensor de luz del dispositivo.
- Consultar un historial de lecturas previas almacenadas en Firebase Cloud Firestore.
- Visualizar en el mapa de GoogleMaps los marcadores de donde fueron realizadas las mediciones.

### Se utilizó
    - Java, Android Studio Quail 3 | 20226.1.3
    - Sensor de luz del dispositivo ('Sensor.Type_LIGHT')
    - Ubicación del dispositivo con GoogleMaps (Play Services Location version 21.4.0)
    - Google Maps SDK for Android
    - Firebase Firestore para almacenamiento de datos en la nube
    - Secrets Gradle plugin para almacera API Keys en secrets.properties

## Requisitos para ejecutar
- Dispositivo físico con sensor de luz integrado y GPS. Los emuladores permiten simular la ubicación, pero sin sensor marcará una lectura de 0 lux.
- Una cuenta de Google para crear el proyecto de Firebase
- Una API Key de Google Maps

## Instrucciones de ejecución
1. Clonar repositorio ```bash git clone URL_REPOSITORIO```
2. Abrir proyecto en Android Studio y esperar la sincronización con Gradle.
3. Configurar Firebase y Google Maps siguiendo las instrucciones de mas abajo. **Sin estos pasos no funciona la app**
4. Conectar un dispositivo o iniciar un emulador en Android Studio y pulsar **Run ▶**
5. Al abrir la app, ingresar a la pantalla registrar lectura y conceder permisos de ubicación cuando se soliciten.

## Configuración de Firebase
Este repositorio **no incluye** el archivo 'google-services.json' real. Se debe crear el personal para hacer uso de la aplicación.

1. Entrar a [Firebase Console](https://console.firebase.google.com) y crear un proyecto.
2. Agregar una app Android con el **nombre de paquete** definido en `build.gradle` (`aplicationId`).
3. Descargar `google-services.json` y colocar en la carpeta `app/`.
4. Activar **Firestore Database**
5. Configurar reglas a su necesidad. La aplicación no implementa el uso de autenticación por lo que si se requiere de autenticar al usuario para establecer una regla mas estricta se debe modificar el servicio de Firebase para permitir la autenticación usada.

## Configuracion de Google Maps SDK
La aplicación hace uso del mapa de Google Maps para mostrar la ubicación de las lecturas en las coordenadas almacenadas en Firebase por lo que se requiere de los servicios de google y la API Key correspondiente.

1. Entrar a [Google Cloud Console](https://console.cloud.google.com/welcome/new) y crear un proyecto.
2. Habilitar Maps SDK for Android en el proyecto y crear la API Key (Opcionalmente se pueden establecer cuotas de consumo por seguridad.)
3. Agregar plugin [Secrets Gradle plugin](https://github.com/google/secrets-gradle-plugin) en `build.gradle.kts` (Module :app):
```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}
```
4. Añadir dependencias de Play Services Maps y Play Services Location de Google en `libs.versions.toml` y sincronizar gradle:
```
[versions]
playServicesMaps = "20.1.0"
playServicesLocation = "21.4.0"

[libraries]
play-services-maps = { group = "com.google.android.gms", name = "play-services-maps", version.ref = "playServicesMaps" }
play-services-location = { group = "com.google.android.gms", name = "play-services-location", version.ref = "playServicesLocation" }
```
5. Crear archivo secrets.properties en la raíz del proyecto y colocar la APi Key dentro `MAPS_API_KEY=TU_CLAVE_API`
6. Verificar que `AndroidManifest.xml` lea la variable utilizando:

```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="\${MAPS_API_KEY}" />
```
7. Agregar secrets {} en el archivo `build.gradle.kts` (Module :app) (El repositorio incluye el archivo local.defaults.properties con una API Key inválida para evitar errores de compilación, par que funcione correctamente se debe establecer la API Key real y funcional en secrets.properties.)
```kotlin
secrets {
    propertiesFileName = "secrets.properties"
    defaultPropertiesFileName = "local.defaults.properties"
}
```


