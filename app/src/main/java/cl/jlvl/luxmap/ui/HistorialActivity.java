package cl.jlvl.luxmap.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cl.jlvl.luxmap.R;
import cl.jlvl.luxmap.adapter.RegistroAdapter;
import cl.jlvl.luxmap.model.Registro;
import cl.jlvl.luxmap.service.ServicioFirebase;

public class HistorialActivity extends AppCompatActivity {

    // La pantalla muestra datos obtenidos de Firebase a modo de lista
    // Se utiliza RecyclerView para personalizar la apariencia de los items de lista
    private RecyclerView recyclerView;
    private RegistroAdapter adaptador;
    private List<Registro> listaRegistros;
    private ServicioFirebase servicioFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_historial);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Se vincula la recyclerView del xml a la propiedad y se configura una disposicion linear para el listado
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Se inicializa la lista de registros vacía para el adaptador y luego se vincula el adaptador con la recyclerview
        listaRegistros = new ArrayList<>();
        adaptador = new RegistroAdapter(listaRegistros);
        recyclerView.setAdapter(adaptador);

        // Se llama al metodo obtenerDatos para cargar los registros en la lista
        servicioFirebase = new ServicioFirebase();
        obtenerDatos();
    }

    // Metodo local para llamar a ObtenerRegistros del servicio de la base de datos
    // Se agregan los datos obtenidos a la lista de registros y se notifica el cambio en los datos
    private void obtenerDatos() {
        servicioFirebase.ObtenerRegistros(new ServicioFirebase.OnObtenerRegistrosCallback() {
            @Override
            public void onSuccess(List<Registro> registrosObtenidos) {
               listaRegistros.clear();
               listaRegistros.addAll(registrosObtenidos);
               adaptador.notifyDataSetChanged();
            }

            // En caso de fallo, se muestra un mensaje en pantalla
            @Override
            public void onFailure(String mensajeError) {
                Toast.makeText(HistorialActivity.this, "Error: " + mensajeError, Toast.LENGTH_SHORT).show();
            }
        });
    }
}