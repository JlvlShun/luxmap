package cl.jlvl.luxmap.model;

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
    // De momento no hay Setters porque solo se utiliza el objeto para ser registrado en Firebase a modo de POJO
    public String getTitulo() { return titulo; }
    public Categoria getCategoria() { return categoria; }
    public Date getFecha() { return fecha; }
    public String getDescripcion() { return descripcion; }
    public double getLatitud() { return latitud; }
    public double getLongitud() { return longitud; }
    public double getLux() { return lux; }

}
