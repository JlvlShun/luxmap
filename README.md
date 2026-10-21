# LuxMap
Aplicación Android que registra mediciones de luminosidad (lux) junto a la ubicación geográfica donde fué realizada la medición.

## Descripción
**La app permite:**
- Registrar una medición que contiene un título, categoría, descripción, coordenadas (latitud y longitud) y nivel de luz en lux, utilizando el sensor de luz del dispositivo.
- Consultar un historial de lecturas previas almacenadas en Firebase Cloud Firestore.
- Visualizar en el mapa de GoogleMaps los marcadores de donde fueron realizadas las mediciones.

- ### Se utilizó
  - Java, Android Studio Quail 3 | 20226.1.3
  - Sensor de luz del dispositivo ('Sensor.Type_LIGHT')
  - Ubicación del dispositivo con GoogleMaps (Play Services Location version 21.4.0)
  - Google Maps SDK for Android
  - Firebase Firestore para almacenamiento de datos en la nube

## Requisitos para ejecutar
- Dispositivo físico con sensor de luz integrado y GPS. Los emuladores permiten simular ubicación pero sin sensor marcará una lectura de 0 lux.
- Una cuenta de Google para crear el proyecto de Firebase
- Una API Key de Google Maps

## Instrucciones de ejecución
1.- Clonar repositorio ```bash git clone URL_REPOSITORIO```
2.- Abrir proyecto en Android Studio y esperar la sincronización con Gradle.
3.- Configurar Firebase y Google Maps siguiendo las instrucciones de mas abajo. **Sin estos pasos no funciona la app**
4.- Conectar un dispositivo o iniciar un emulador en Android Studio y pulsar **Run ▶**
5.- Al abrir la app, ingresar a la pantalla registrar lectura y conceder permisos de ubicación cuando se soliciten.

## Configuración de Firebase
Este repositorio **no incluye** el archivo 'google-services.json' real. Se debe crear el personal para hacer uso de la aplicación.

1.- Entrar a [Firebase Console](https://console.firebase.google.com) y crear un proyecto.
2.- Agregar una app Android con el **nombre de paquete** definido en `build.gradle` (`aplicationId`).
3.- Descargar `google-services.json` y colocar en la carpeta `app/`.
4.- Activar **Firestore Database**

