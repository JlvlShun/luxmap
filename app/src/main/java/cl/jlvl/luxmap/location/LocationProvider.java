package cl.jlvl.luxmap.location;

import android.annotation.SuppressLint;
import android.content.Context;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class LocationProvider {

    // Código asociado a la petición de los permisos (puede ser cualquiera)
    private static final int PERMISSION_REQUEST_CODE = 100;

    // Cliente de ubicación de Google
    private final FusedLocationProviderClient fusedLocationProviderClient;

    // interfaz de Callbacks para comunicar los datos/estado obtenidos a la pantalla donde se llame
    public interface UbicacionCallback {
        void onUbicacionSuccess(double latitud, double longitud);
        void onUbicacionFail(String mensajeError);
    }

    // El constructor recibe el contexto de la app donde es instanciado (el activity.this)
    public LocationProvider(Context context) {
        // Inicializamos el cliente en el constructor
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }

    // El @SuppressLint silencia en android studio la advertencia de que no se tienen los permisos
    // RegistrarActivity que incluye una comprobación de permisos antes de llamar este metodo
    @SuppressLint("MissingPermission")
    public void obtenerCoordenadasActuales(UbicacionCallback callback) {
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(location -> {
                   if (location != null) {
                       callback.onUbicacionSuccess(location.getLatitude(), location.getLongitude());
                   } else {
                       callback.onUbicacionFail("No se pudo obtener la ubicación.");
                   }
                });
    }
}
