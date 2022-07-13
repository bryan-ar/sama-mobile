package com.android.sama.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.sama.OrdenActivity;
import com.android.sama.R;
import com.android.sama.modals.CalificarModal;
import com.android.sama.modals.TiempoLlegadaModal;
import com.android.sama.models.PedidoModel;

import java.util.ArrayList;

public class PedidosAdapter extends RecyclerView.Adapter<PedidosAdapter.Viewholder> {

    private Context context;
    private ArrayList<PedidoModel> pedidos;

    // Constructor
    public PedidosAdapter(Context context, ArrayList<PedidoModel> mPedidos) {
        this.context = context;
        this.pedidos = mPedidos;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedido, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        PedidoModel model = pedidos.get(position);
        holder.tvPedido.setText("Pedido " + model.getId());
        holder.tvDetalle.setText(model.getDescripcion());

        holder.linearLayout4.setOnClickListener(view -> {
            if(model.getEstado() == 0){
                FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
                TiempoLlegadaModal mTiempoLlegadaModal = TiempoLlegadaModal.newInstance(model.getId());
                mTiempoLlegadaModal.show(fm, "fragment_dialog");
            }else if(model.getEstado() == 1){
                FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
                CalificarModal mCalificarModal = CalificarModal.newInstance(model.getId());
                mCalificarModal.show(fm, "fragment_dialog");
            }
        });

        holder.ivVer.setOnClickListener(view -> {
            context = view.getContext();
            context.startActivity(new Intent(context, OrdenActivity.class)
                    .putExtra("nuevo_pedido", 0).putExtra("id_pedido", model.getId()));
        });
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return pedidos.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView tvPedido, tvDetalle;
        private AppCompatImageView ivVer;
        private LinearLayout linearLayout4;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            tvPedido = itemView.findViewById(R.id.tvPedido);
            tvDetalle = itemView.findViewById(R.id.tvDetalle);
            ivVer = itemView.findViewById(R.id.tvVer);
            linearLayout4 = itemView.findViewById(R.id.linearLayout4);
        }
    }
}
