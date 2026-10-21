package cl.jlvl.luxmap.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorLuzManager implements SensorEventListener {
    // La interfaz SensorEventListener requiere de implementar los métodos para los eventos onSensorChanged y onAccuracyChanged

    // Esta interfaz contiene eventos para comunicar el valor de luz percibido por el sensor de forma activa y otro para el caso en que no se cuente con un sensor en el dispositivo
    public interface OnCambioDeLuxListener {
        void onValorCambiado(float luxes);
        void onSensorNoDisponible();
    }

    private final SensorManager sensorManager;
    private Sensor sensorDeLux;
    private final OnCambioDeLuxListener listener;

    // El constructor recibe el contexto de la aplicación (el Activity.this) y el listener que va a estar pendiente del evento OnCambioDeLuxListener para comunicar los datos del sensor
    public SensorLuzManager(Context context, OnCambioDeLuxListener listener) {
        // Se vincula el listener externo al propio de la clase y el SENSOR_SERVICE obtenido a través del contexto
        this.listener = listener;
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        // Se obtiene el sensor de luz y se guarda localmente
        if (sensorManager != null) {
            this.sensorDeLux = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        }

        // Si existe un listener activo, pero no se encuentra el sensor, se llama al evento onSensorNoDisponible
        if (this.sensorDeLux == null && listener != null) {
            listener.onSensorNoDisponible();
        }
    }

    // Los métodos iniciarSensor y detenerSensor registran o eliminan el registro del listener al sensor y los invocados en onResume y onPause en el lugar de uso
    public void iniciarSensor() {
        if (sensorManager != null && sensorDeLux != null) {
            sensorManager.registerListener(this, sensorDeLux, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void detenerSensor() {
        if (sensorManager != null && sensorDeLux != null) {
            sensorManager.unregisterListener(this);
        }
    }

    // El evento onSensorChanged expone el primer valor del arreglo siendo este el valor más actual de luz medida por el sensor
    // Se expone ese valor a través de onValorCambiado
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT && listener != null) {
            float valorLux = event.values[0];
            listener.onValorCambiado(valorLux);
        }
    }

    // NOTA PERSONAL
    // onAccuracyChanged es la precisión de lectura del sensor cambia
    // Existen cuatro constantes predefinidas para el valor de precisión:
    // SensorManager.SENSOR_STATUS_ACCURACY_HIGH, SENSOR_STATUS_ACCURACY_MEDIUM, SENSOR_STATUS_ACCURACY_HIGH y SENSOR_STATUS_ACCURACY_UNRELIABLE con valores 3, 2, 1 y 0 respectivamente
    // por lo que se podría comprobar la precisión con: if (accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) para comunicar que los datos no son fiables por no ser exactos o incluso
    // comunicar la precisión actual a modo de advertencia.
    // En este caso no se utiliza asi que queda en blanco por ser requerido por la interfaz
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
