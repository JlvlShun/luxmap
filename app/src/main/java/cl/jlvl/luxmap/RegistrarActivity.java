package cl.jlvl.luxmap;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Date;

import cl.jlvl.luxmap.model.Categoria;
import cl.jlvl.luxmap.model.Registro;
import cl.jlvl.luxmap.model.SensorLux;
import cl.jlvl.luxmap.service.FirebaseService;

public class RegistrarActivity extends AppCompatActivity {
    // Instancia del servicio de Firebase del constructor Firebase.java
    private final FirebaseService servicioFirebase = new FirebaseService();
    // Import de elementos de la interfaz gráfica
    private EditText etTitulo, etDescripcion;
    private TextView tvLux;
    private Button btnRegistrar;
    private Spinner spCategoria;
    // Aún no hay servicio de GoogleMaps asi que por el momento, almaceno en memoria los valores de las coordenadas (Chillán)
    // TODO: Reemplazar esto por los valores obtenidos del gps de GoogleMaps
    private final double EjemploLatitud = -36.60667;
    private final double EjemploLongitud = -72.10333;
    private SensorLux sensorLux;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar);

        // Vinculación de elementos de la interfaz con las variables declaradas
        etTitulo = findViewById(R.id.etNombre);
        etDescripcion = findViewById(R.id.ettmDescripcion);
        tvLux = findViewById(R.id.tvLux);
        spCategoria = findViewById(R.id.spCategoria);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        // Cargo las categorías en el spinner
        CargarCategorias();

        // Instancia de la clase que maneja el sensor de luz
        // la clase expone dos eventos, uno para cuando cambia el valor de luz percibida por el sensor y otro para cuando no se encuentra el sensor en el dispositivo
        sensorLux = new SensorLux(this, new SensorLux.OnCambioDeLuxListener() {
            @Override
            public void onValorCambiado(float luxes) {
                tvLux.setText(String.valueOf(luxes));
            }

            @Override
            public void onSensorNoDisponible() {
                tvLux.setText(getString(R.string.mensaje_sin_sensor));
            }
        });

        // Al presionar el boton de Registro se capturan los datos de los EditTexts, los valores de los TextViews, la fecha actual con Date() y futuramente el valor actual de luz del sensor
        // Se usan esos datos para crear un objeto Registro para entregárselo al servicio que lo guarda en Firebase

        btnRegistrar.setOnClickListener(v-> {
            String titulo = etTitulo.getText().toString().trim();
            String descripcion = etDescripcion.getText().toString().trim();
            String categoria = spCategoria.getSelectedItem().toString();
            Date fechaActual = new Date();
            double lux = Double.parseDouble(tvLux.getText().toString()); // <- Tiene un valor por defecto de 0 usando el placeholder de 'res/values/strings'
            Categoria categoriaEnum = Categoria.valueOf(categoria.trim()); // <- Se convierte el string categoría en el enum de Categoría para crear el objeto Registro

            // Creación del objeto
            Registro registro = new Registro(titulo, categoriaEnum, fechaActual, descripcion, EjemploLatitud, EjemploLongitud, lux);

            // Carga en Firebase usando el servicio de FirebaseService.java
            // El servicio responde con dos posibles eventos, onSuccess y onFailure dependiendo de si se logró guardar en la base de datos o no
            servicioFirebase.GuardarRegistro(registro, new FirebaseService.OnFirebaseCallback() {
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

    // Overrides de los métodos de AppCompatActivity
    // Al bloquear/desbloquear el teléfono se llaman a los métodos para activa/desactivar el listener del sensor
    @Override
    protected void onResume() {
        super.onResume();
        sensorLux.iniciarSensor();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorLux.detenerSensor();
    }
}