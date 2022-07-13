package com.android.sama.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.sama.R;
import com.android.sama.models.OfertaModel;

import java.util.ArrayList;

public class OfertasAdapter extends RecyclerView.Adapter<OfertasAdapter.Viewholder> {

    private Context context;
    private ArrayList<OfertaModel> ofertas;

    // Constructor
    public OfertasAdapter(Context context, ArrayList<OfertaModel> mOfertas) {
        this.context = context;
        this.ofertas = mOfertas;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_oferta, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        OfertaModel model = ofertas.get(position);
        holder.tvOfertaNom.setText(model.getNombre());
        holder.tvOfertaDesc.setText(model.getDescripcion());
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return ofertas.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView tvOfertaNom, tvOfertaDesc;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            tvOfertaNom = itemView.findViewById(R.id.tvOfertaNom);
            tvOfertaDesc = itemView.findViewById(R.id.tvOfertaDesc);
        }
    }
}
