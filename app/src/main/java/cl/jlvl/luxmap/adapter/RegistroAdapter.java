package cl.jlvl.luxmap.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cl.jlvl.luxmap.R;
import cl.jlvl.luxmap.model.Registro;


public class RegistroAdapter extends RecyclerView.Adapter<RegistroAdapter.RegistroViewHolder> {
    // Lista para almacenar los registros de Firebase
    private final List<Registro> listaRegistros;

    // El constructor recibe la lista externa y la vincula a la propiedad
    public RegistroAdapter(List<Registro> listaRegistros) {
        this.listaRegistros = listaRegistros;
    }

    // Metodo para crear la ViewHolder que contiene los elementos de la UI (Los TextViews del listado)
    @NonNull
    @Override
    public RegistroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_registro, parent, false);
        return new RegistroViewHolder(view);
    }

    // onBindViewHolder asocia los datos obtenidos del objeto Registro con la ViewHolder
    @Override
    public void onBindViewHolder(@NonNull RegistroViewHolder holder, int position) {
        Registro registro = listaRegistros.get(position);

        holder.tvTitulo.setText(registro.getTitulo());
        holder.tvLatitud.setText(String.valueOf(registro.getLatitud()));
        holder.tvLongitud.setText(String.valueOf(registro.getLongitud()));
    }

    // getItemCount obtiene el tamaño o largo de datos a mostrar en la lista, requerido por RecyclerView
    @Override
    public int getItemCount() {
        return listaRegistros != null ? listaRegistros.size() : 0;
    }

    // La clase ViewHolder que contiene los elementos de la UI y es creada con onCreateViewHolder
    public static class RegistroViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvLatitud, tvLongitud;

        public RegistroViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitulo = itemView.findViewById(R.id.tvTituloRegistro);
            tvLatitud = itemView.findViewById(R.id.tvLatitudRegistro);
            tvLongitud = itemView.findViewById(R.id.tvLongitudRegistro);
        }
    }
}
