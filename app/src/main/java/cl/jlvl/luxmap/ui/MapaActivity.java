package cl.jlvl.luxmap.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import cl.jlvl.luxmap.R;
import cl.jlvl.luxmap.model.Registro;
import cl.jlvl.luxmap.service.ServicioFirebase;

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private ServicioFirebase servicioFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mapa);

        servicioFirebase = new ServicioFirebase();

        // Se vincula el mapFrament del xml para ser usado como contenedor
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFramentContainer);

        // Carga el mapa de Google de forma asíncrona en la pantalla
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Al terminar de cargar el mapa se vincula el mapa a la propiedad googleMap de la clase y se llama al metodo para cargar los puntos en el mapa
    @Override
    public void onMapReady(@NonNull GoogleMap gMap) {
        googleMap = gMap;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        cargarPuntosRegistros();
    }

    // Se obtienen los datos de las coordenadas de los puntos desde Firebase usando este metodo
    private void cargarPuntosRegistros() {
        servicioFirebase.ObtenerRegistros(new ServicioFirebase.OnObtenerRegistrosCallback() {
            @Override
            public void onSuccess(List<Registro> listaRegistros) {
                if (listaRegistros == null || listaRegistros.isEmpty()) {
                    Toast.makeText(MapaActivity.this,"No hay registros para mostrar.", Toast.LENGTH_SHORT).show();
                    return;
                }

                LatLng ultimaCoordenada = null;

                // La posicion LatLng utiliza los datos de latitud y longitud de los registros para posicionar los puntos marcadores
                // Los puntos contienen un titulo obtenido del titulo del registro y una descripcion que muestra la cantidad de Lux medidos en el lugar
                for (Registro registro : listaRegistros) {
                    LatLng posicion = new LatLng(registro.getLatitud(), registro.getLongitud());
                    String snippetInfo = "Lux: " + registro.getLux() + " - ";
                    googleMap.addMarker(new MarkerOptions()
                            .position(posicion)
                            .title(registro.getTitulo())
                            .snippet(snippetInfo));

                    ultimaCoordenada = posicion;
                }

                // Se posiciona la camara del mapa a la última coordenada obtenida del último punto
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ultimaCoordenada, 12f));

            }


            // En caso de no encontrar datos, se muestra un mensaje en pantalla con el error
            @Override
            public void onFailure(String mensajeError) {
                Toast.makeText(MapaActivity.this, "Error al cargar puntos en el mapa: " + mensajeError, Toast.LENGTH_SHORT).show();
            }
        });
    }
}