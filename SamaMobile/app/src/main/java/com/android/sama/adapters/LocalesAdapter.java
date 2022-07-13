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

import com.android.sama.MapsActivity;
import com.android.sama.R;
import com.android.sama.models.LocalModel;

import java.util.ArrayList;

public class LocalesAdapter extends RecyclerView.Adapter<LocalesAdapter.Viewholder> {

    private Context context;
    private ArrayList<LocalModel> locales;

    // Constructor
    public LocalesAdapter(Context context, ArrayList<LocalModel> mLocales) {
        this.context = context;
        this.locales = mLocales;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_local, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        LocalModel model = locales.get(position);
        holder.tvNombre.setText(model.getNombre());
        holder.tvDireccion.setText(model.getDireccion());

        if(model.getFavorite() == 0){
            holder.ivFavorito.setImageResource(R.drawable.icon_favorite_border);
        }else{
            holder.ivFavorito.setImageResource(R.drawable.icon_favorite);
        }

        holder.ivMaps.setOnClickListener(view -> {
            Intent intent = new Intent(context, MapsActivity.class);
            intent.putExtra("latitude",model.getGps_lat());
            intent.putExtra("longitude",model.getGps_lon());
            intent.putExtra("nombre_local", model.getNombre());

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return locales.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView tvNombre, tvDireccion;
        private ImageView ivFavorito, ivMaps;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreLocal);
            tvDireccion = itemView.findViewById(R.id.tvDireccionLocal);
            ivFavorito = itemView.findViewById(R.id.ivFavorito);
            ivMaps = itemView.findViewById(R.id.img_maps);
        }
    }
}
