package cl.jlvl.luxmap.service;

import com.google.firebase.firestore.FirebaseFirestore;

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

    // Metodo para guardar el registro en Firebase
    // Recibe un objeto creado a partir de los datos del formulario y de los valores obtenidos de las coordenadas y el sensor de luz
    // También recibe una instancia del callback para la comunicación de datos
    public void GuardarRegistro(Registro registro, OnFirebaseCallback callback) {

        basedatos.collection("registros").add(registro)
                .addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

}
