package cl.jlvl.luxmap.service;

import com.google.firebase.firestore.FirebaseFirestore;

import cl.jlvl.luxmap.model.Registro;

public class FirebaseService {
    private final FirebaseFirestore basedatos;

    public FirebaseService() {
        this.basedatos = FirebaseFirestore.getInstance();
    }

    public interface OnFirebaseCallback {
        void onSuccess();
        void onFailure(String mensajeError);
    }
    public void GuardarRegistro(Registro registro, OnFirebaseCallback callback) {

        basedatos.collection("registros").add(registro)
                .addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

}
