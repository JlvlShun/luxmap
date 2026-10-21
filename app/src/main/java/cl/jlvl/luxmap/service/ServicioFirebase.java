package cl.jlvl.luxmap.service;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import cl.jlvl.luxmap.model.Registro;

public class ServicioFirebase {
    private final FirebaseFirestore basedatos;

    // El constructor asigna la instancia al objeto al momento de instanciar
    public ServicioFirebase() {
        this.basedatos = FirebaseFirestore.getInstance();
    }

    // Intefaz de callbacks para comunicar el estado al registrar datos en Firebase
    public interface OnFirebaseCallback {
        void onSuccess();
        void onFailure(String mensajeError);
    }

    // Interfaz para comunicar la obtención de la lista de registros
    public interface OnObtenerRegistrosCallback {
        void onSuccess(List<Registro> listaRegistros);
        void onFailure(String mensajeError);
    }

    // Metodo para guardar el registro en Firebase
    // Recibe un objeto creado a partir de los datos del formulario y de los valores obtenidos de las coordenadas y el sensor de luz
    // También recibe una instancia del callback para la comunicación de datos
    public void GuardarRegistro(Registro registro, OnFirebaseCallback callback) {

        basedatos.collection("registros").add(registro)
                .addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    // Metodo para obtener los registros desde Firebase
    // Se consulta a la coleccion registros y se almacenan los datos en la lista de Registros
    // Al recuperarse los datos, se provee la lista de registros a través de la interfaz OnObtenerRegistrosCallback
    public void ObtenerRegistros(OnObtenerRegistrosCallback callback) {
        basedatos.collection("registros").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Registro> registros = new ArrayList<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Registro registro = document.toObject(Registro.class);
                        registros.add(registro);
                    }

                    callback.onSuccess(registros);
                }).addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }
}
