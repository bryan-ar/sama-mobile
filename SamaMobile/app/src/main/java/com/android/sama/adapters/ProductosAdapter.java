package com.android.sama.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.sama.R;
import com.android.sama.dao.OrdenDAO;
import com.android.sama.dao.PedidoDAO;
import com.android.sama.models.PedidoModel;
import com.android.sama.models.PedidoOrdenModel;
import com.android.sama.models.ProductoModel;
import com.android.sama.utils.Constants;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.RecyclerViewHolder> {
    private ArrayList<ProductoModel> productos;
    private Context mcontext;
    private int id_usuario;
    private PedidoDAO pedidoDAO;
    private OrdenDAO ordenDAO;

    public ProductosAdapter(ArrayList<ProductoModel> mProductos, Context mcontext) {
        this.productos = mProductos;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);

        pedidoDAO = new PedidoDAO(parent.getContext());
        ordenDAO = new OrdenDAO(parent.getContext());

        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        // Set the data to textview and imageview.
        ProductoModel model = productos.get(position);
        holder.tvNombreProducto.setText(model.getDescripcion());
        holder.tvPrecio.setText("S/" + model.getPrecio());

        int imagen;
        if(model.getId() == 1){
            imagen = R.drawable.jugo_naranja;
        }else if(model.getId() == 2){
            imagen = R.drawable.hamburguesa;
        }else if(model.getId() == 3){
            imagen = R.drawable.tequenos;
        }else{
            imagen = R.drawable.not_found;
        }

        holder.ivProducto.setImageResource(imagen);

        holder.ivAdicionar.setOnClickListener(view -> {
            //validar en bd si tiene orden
            SharedPreferences myPrefs = mcontext.getSharedPreferences(Constants.PrefsLogin, Context.MODE_PRIVATE);
            int id_usuario = Integer.parseInt(myPrefs.getString("id_usuario","0"));

            PedidoModel pedido = pedidoDAO.obtenerPedidoUsuario(id_usuario);

            if(pedido.getIdUsuario() == 0){
                //crear pedido
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = new Date();
                String fecha_creacion = dateFormat.format(date);

                date = new Date();
                date.setHours(1);
                String hora_llegada = dateFormat.format(date);

                pedidoDAO.insertarPedido(new PedidoModel(
                    0, id_usuario, fecha_creacion,
                        "Compra ordinaria | " + fecha_creacion.substring(0,10) + " | S/" + model.getPrecio(),
                        model.getPrecio(), 0, 0, hora_llegada
                ));

                //crear orden para el producto en 1
                ordenDAO.insertarItem(new PedidoOrdenModel(
                    0, model.getId(), model.getDescripcion(), 1, model.getPrecio(), model.getPrecio()
                ));

                Toast.makeText(mcontext, "Producto agregado", Toast.LENGTH_SHORT).show();
            }else{
                //validar si la orden contiene el producto
                PedidoOrdenModel orden = ordenDAO.obtenerItem(model.getId());

                if(orden.getIdProducto() != 0){
                    //si: aumentar
                    orden.aumentar();
                    ordenDAO.actualizarItem(orden);
                }else{
                    //no: insertar
                    ordenDAO.insertarItem(new PedidoOrdenModel(
                            0, model.getId(), model.getDescripcion(), 1, model.getPrecio(), model.getPrecio()
                    ));
                }

                ArrayList<PedidoOrdenModel> ordenDetalle = ordenDAO.obtenerOrden();
                double monto_total = 0;
                for (PedidoOrdenModel x : ordenDetalle) {
                    monto_total += x.getMontoPagar();
                }

                pedido.setMonto_total(monto_total);
                pedido.setDescripcion("Compra ordinaria | " +
                        pedido.getFechaCreacion().substring(0,10) + " | S/" + monto_total);
                pedidoDAO.actualizarPedido(pedido);

                Toast.makeText(mcontext, "Producto agregado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        // this method returns the size of recyclerview
        return productos.size();
    }

    // View Holder Class to handle Recycler View.
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView tvNombreProducto;
        private TextView tvPrecio;
        private ImageView ivProducto,ivAdicionar;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreProducto = itemView.findViewById(R.id.tvNombreProducto);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            ivProducto = itemView.findViewById(R.id.ivProducto);
            ivAdicionar = itemView.findViewById(R.id.ivAdicionar);
        }
    }
}
