package cl.jlvl.luxmap.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Date;

import cl.jlvl.luxmap.R;
import cl.jlvl.luxmap.model.Categoria;
import cl.jlvl.luxmap.model.Registro;
import cl.jlvl.luxmap.sensor.SensorLuzManager;
import cl.jlvl.luxmap.service.ServicioFirebase;
import cl.jlvl.luxmap.location.LocationProvider;

public class RegistrarActivity extends AppCompatActivity {

    // Instancia del servicio de Firebase del constructor Firebase.java
    private final ServicioFirebase servicioFirebase = new ServicioFirebase();

    // Import de elementos de la interfaz gráfica
    private EditText etTitulo, etDescripcion;
    private TextView tvLux, tvLatitud, tvLongitud, tvTituloCoords;
    private Button btnRegistrar;
    private Spinner spCategoria;

    private boolean coordenadasObtenidas = false;
    private SensorLuzManager sensorLuzManager;
    private LocationProvider locationProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar);

        // Vinculación de elementos de la interfaz con las variables declaradas
        etTitulo = findViewById(R.id.etNombre);
        etDescripcion = findViewById(R.id.ettmDescripcion);
        tvLux = findViewById(R.id.tvLux);
        tvTituloCoords = findViewById(R.id.tvCoordenadas);
        tvLatitud = findViewById(R.id.tvLatitud);
        tvLongitud = findViewById(R.id.tvLongitud);
        spCategoria = findViewById(R.id.spCategoria);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        // Cargo las categorías en el spinner
        CargarCategorias();

        // Instancia de la clase que maneja el sensor de luz
        // la clase expone dos eventos, uno para cuando cambia el valor de luz percibida por el sensor y otro para cuando no se encuentra el sensor en el dispositivo
        sensorLuzManager = new SensorLuzManager(this, new SensorLuzManager.OnCambioDeLuxListener() {
            @Override
            public void onValorCambiado(float luxes) {
                tvLux.setText(String.valueOf(luxes));
            }

            @Override
            public void onSensorNoDisponible() {
                tvLux.setText(getString(R.string.mensaje_sin_sensor));
            }
        });

        locationProvider = new LocationProvider(this);
        if (permisosConcedidos()) {
            obtenerCoordenadas();
        } else {
            ActivityCompat.requestPermissions(RegistrarActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }

        // Al presionar el boton de Registro se capturan los datos de los EditTexts, los valores de los TextViews, la fecha actual con Date() y futuramente el valor actual de luz del sensor
        // Se usan esos datos para crear un objeto Registro para entregárselo al servicio que lo guarda en Firebase
        btnRegistrar.setOnClickListener(v-> {
            // Se verifica primero que se obtuvieron las coordenadas, de lo contrario salta un aviso en pantalla y no se procede con el registro
            if (!coordenadasObtenidas) {
                Toast.makeText(RegistrarActivity.this, "No se han obtenido las coordenadas actuales.", Toast.LENGTH_SHORT).show();
                return;
            }

            // En caso de que las coordenadas se obtuvieron, se procede a crear el registro
            String titulo = etTitulo.getText().toString().trim();
            String descripcion = etDescripcion.getText().toString().trim();
            Categoria categoria = Categoria.valueOf(spCategoria.getSelectedItem().toString()) ;
            Date fechaActual = new Date();
            double latitud = Double.parseDouble(tvLatitud.getText().toString());
            double longitud = Double.parseDouble(tvLongitud.getText().toString());
            double lux = Double.parseDouble(tvLux.getText().toString()); // <- Tiene un valor por defecto de 0 usando el placeholder de 'res/values/strings'


            // Creación del objeto
            Registro registro = new Registro(titulo, categoria, fechaActual, descripcion, latitud, longitud, lux);

            // Carga en Firebase usando el servicio de FirebaseService.java
            // El servicio responde con dos posibles eventos, onSuccess y onFailure dependiendo de si se logró guardar en la base de datos o no
            servicioFirebase.GuardarRegistro(registro, new ServicioFirebase.OnFirebaseCallback() {
                @Override
                public void onSuccess() {
                   Toast.makeText(RegistrarActivity.this, "Registro guardado", Toast.LENGTH_SHORT).show();
                   finish(); // <- Al registrar, se finaliza la pantalla y vuelve al menú principal
                }

                @Override
                public void onFailure(String mensajeError) {
                    Toast.makeText(RegistrarActivity.this, "Error al guardar: " + mensajeError, Toast.LENGTH_SHORT).show();
                }
            });
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Separé el bloque que carga las categorías del spinner para limpiar un poco el onCreate
    // Las categorías se obtienen del 'res/values/strings.xml' que contiene un string array con los valores del enum Categoria como items
    private void CargarCategorias() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.arreglo_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(adapter);
    }

    // Se obtienen las coordenadas utilizando el metodo de LocationProvider y comunicamos los resultados con el callback
    private void obtenerCoordenadas() {
        locationProvider.obtenerCoordenadasActuales(new LocationProvider.UbicacionCallback() {
            @Override
            public void onUbicacionSuccess(double latitud, double longitud) {
                tvLatitud.setText(String.valueOf(latitud));
                tvLongitud.setText(String.valueOf(longitud));
                coordenadasObtenidas = true;
            }

            @Override
            public void onUbicacionFail(String mensajeError) {
                Toast.makeText(RegistrarActivity.this, mensajeError, Toast.LENGTH_SHORT).show();
                tvTituloCoords.setText(R.string.mensaje_sin_coordenadas);
                coordenadasObtenidas = false;
            }
        });
    }

    // Se revisa que se cuente con los permisos de ubicacion para permitir la captura de la ubicación
    private boolean permisosConcedidos() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    // Overrides de los métodos de AppCompatActivity
    // Al bloquear/desbloquear el teléfono se llaman a los métodos para activa/desactivar el listener del sensor
    @Override
    protected void onResume() {
        super.onResume();
        sensorLuzManager.iniciarSensor();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorLuzManager.detenerSensor();
    }
}