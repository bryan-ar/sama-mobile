package com.android.sama.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.sama.R;
import com.android.sama.dao.OrdenDAO;
import com.android.sama.dao.PedidoDAO;
import com.android.sama.interfaces.DataTransferInterface;
import com.android.sama.models.PedidoModel;
import com.android.sama.models.PedidoOrdenModel;
import com.android.sama.utils.Constants;

import java.util.ArrayList;

public class OrdenNuevaAdapter extends RecyclerView.Adapter<OrdenNuevaAdapter.Viewholder> {

    private Context context;
    private ArrayList<PedidoOrdenModel> orden;
    DataTransferInterface dtInterface;
    private PedidoDAO pedidoDAO;
    private OrdenDAO ordenDAO;
    private int id_usuario;

    // Constructor
    public OrdenNuevaAdapter(Context context, ArrayList<PedidoOrdenModel> mOrden, DataTransferInterface dtInterface) {
        this.context = context;
        this.orden = mOrden;
        this.dtInterface = dtInterface;

        SharedPreferences myPrefs = context.getSharedPreferences(Constants.PrefsLogin, Context.MODE_PRIVATE);
        id_usuario = Integer.parseInt(myPrefs.getString("id_usuario","0"));
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nueva_orden, parent, false);
        pedidoDAO = new PedidoDAO(parent.getContext());
        ordenDAO = new OrdenDAO(parent.getContext());
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        PedidoOrdenModel model = orden.get(position);
        holder.tvProducto.setText(model.getNombreProducto());
        holder.tvDetalle.setText("Precio: S/" + model.getPrecio() + " | Sub Total: S/" + model.getMontoPagar());

        holder.ivMenos.setOnClickListener(view -> {
            model.restar();
            ordenDAO.actualizarItem(model);

            if(model.getCantidad() == 0){
                //Eliminar producto
                orden.remove(model);
            }

            ArrayList<PedidoOrdenModel> ordenDetalle = ordenDAO.obtenerOrden();
            double monto_total = 0;
            for (PedidoOrdenModel x : ordenDetalle) {
                monto_total += x.getMontoPagar();
            }

            PedidoModel pedido = pedidoDAO.obtenerPedidoUsuario(id_usuario);

            pedido.setMonto_total(monto_total);
            pedidoDAO.actualizarPedido(pedido);

            notifyDataSetChanged();
        });

        holder.ivMas.setOnClickListener(view -> {
            model.aumentar();
            ordenDAO.actualizarItem(model);

            ArrayList<PedidoOrdenModel> ordenDetalle = ordenDAO.obtenerOrden();
            double monto_total = 0;
            for (PedidoOrdenModel x : ordenDetalle) {
                monto_total += x.getMontoPagar();
            }

            PedidoModel pedido = pedidoDAO.obtenerPedidoUsuario(id_usuario);

            pedido.setMonto_total(monto_total);
            pedidoDAO.actualizarPedido(pedido);

            notifyDataSetChanged();
        });

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

        holder.tvCantidad.setText("" + model.getCantidad());

        dtInterface.actualizarTotalPagar();
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return orden.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView tvProducto, tvDetalle, tvCantidad;
        private AppCompatImageView ivMenos, ivMas;
        private ImageView img_producto;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            tvProducto = itemView.findViewById(R.id.tvProducto);
            tvDetalle = itemView.findViewById(R.id.tvDetalle);
            tvCantidad = itemView.findViewById(R.id.tvCantidad);
            ivMenos = itemView.findViewById(R.id.ivMenos);
            ivMas = itemView.findViewById(R.id.ivMas);
            img_producto = itemView.findViewById(R.id.img_producto);
        }
    }
}
