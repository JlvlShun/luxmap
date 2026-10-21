package cl.jlvl.luxmap.model;

import com.google.firebase.firestore.Exclude;

import java.util.Date;

// Este objeto se utilizará para centralizar los datos de cada registro de lectura a ingresar en Firebase
public class Registro {
    private String titulo;
    private Categoria categoria;
    private Date fecha;
    private String descripcion;
    private double latitud;
    private double longitud;
    private double lux;

    // El constructor vacío es para Firebase
    public Registro() {}

    // Se utiliza este constructor para instanciar y registrar en Firebase
    public Registro(String titulo, Categoria categoria, Date fecha, String descripcion, double latitud, double longitud, double lux) {
        this.titulo = (titulo == null || titulo.trim().isEmpty()) ? "Sin título" : titulo;
        this.categoria = categoria;
        this.fecha = fecha;
        this.descripcion = (descripcion == null || descripcion.trim().isEmpty()) ? "Sin descripción" : descripcion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.lux = lux;
    }


    // Getters
    public String getTitulo() { return titulo; }

    // Se excluye el atributo enum al serializar
    @Exclude
    public Categoria getCategoria() { return categoria; }

    public Date getFecha() { return fecha; }
    public String getDescripcion() { return descripcion; }
    public double getLatitud() { return latitud; }
    public double getLongitud() { return longitud; }
    public double getLux() { return lux; }

    // Setters
    // Setter excluido para Firebase (Firebase no entrega el enum categoría por lo que se debe utilizar un setter que reciba un String)
    @Exclude
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setLatitud(double latitud) { this.latitud = latitud; }
    public void setLongitud(double longitud) { this.longitud = longitud; }
    public void setLux(double lux) { this.lux = lux; }

    @com.google.firebase.firestore.PropertyName("categoria")
    public void setCategoriaFromFirebase(String categoriaString) {
        this.categoria = Categoria.valueOf(categoriaString);
    }
}
