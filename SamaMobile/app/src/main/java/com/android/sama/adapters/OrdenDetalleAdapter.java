package com.android.sama.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.sama.R;
import com.android.sama.models.PedidoOrdenModel;

import java.util.ArrayList;

public class OrdenDetalleAdapter extends RecyclerView.Adapter<OrdenDetalleAdapter.Viewholder> {

    private Context context;
    private ArrayList<PedidoOrdenModel> orden;

    // Constructor
    public OrdenDetalleAdapter(Context context, ArrayList<PedidoOrdenModel> mOrden) {
        this.context = context;
        this.orden = mOrden;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orden_detalle, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        PedidoOrdenModel model = orden.get(position);
        holder.tvProducto.setText(model.getNombreProducto());
        holder.tvDetalle.setText("Cantidad: " + model.getCantidad() + " | Precio: S/" + model.getPrecio() + " | Sub Total: S/" + model.getMontoPagar());

        int imagen;
        if(model.getIdProducto() == 1){
            imagen = R.drawable.jugo_naranja;
        }else if(model.getIdProducto() == 2){
            imagen = R.drawable.hamburguesa;
        }else if(model.getIdProducto() == 3){
            imagen = R.drawable.tequenos;
        }else{
            imagen = R.drawable.not_found;
        }

        holder.img_producto.setImageResource(imagen);
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return orden.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView tvProducto, tvDetalle;
        private ImageView img_producto;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            tvProducto = itemView.findViewById(R.id.tvProducto);
            tvDetalle = itemView.findViewById(R.id.tvDetalle);
            img_producto = itemView.findViewById(R.id.img_producto);
        }
    }
}
