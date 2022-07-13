package com.android.sama;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.sama.utils.Constants;

public class MenuActivity extends AppCompatActivity {

    LinearLayout menu1, menu2, menu3;
    private TextView tv_nombres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        setupMenus();

        SharedPreferences myPrefs = this.getSharedPreferences(Constants.PrefsLogin, Context.MODE_PRIVATE);
        String nombres = myPrefs.getString("nombres",null);
        String apellido_paterno = myPrefs.getString("apellido_paterno",null);
        String apellido_materno = myPrefs.getString("apellido_materno",null);

        if(nombres == null){
            startActivity(new Intent(MenuActivity.this, LoginActivity.class));
            finish();

            Toast.makeText(MenuActivity.this,"Sesión perdida", Toast.LENGTH_SHORT).show();
        }

        tv_nombres = findViewById(R.id.tv_nombres);
        tv_nombres.setText(nombres + " " + apellido_paterno + " " + apellido_materno);

        ImageView ivCerrarSesion = findViewById(R.id.ivCerrarSesion);
        ivCerrarSesion.setOnClickListener(view -> {
            SharedPreferences pref = getSharedPreferences(Constants.PrefsLogin, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();

            startActivity(new Intent(MenuActivity.this, LoginActivity.class));
            finish();

            Toast.makeText(MenuActivity.this,"Espero verlo pronto", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupMenus(){
        menu1 = findViewById(R.id.menu1);
        menu2 = findViewById(R.id.menu2);
        menu3 = findViewById(R.id.menu3);

        menu1.setOnClickListener(view -> {
            startActivity(new Intent(MenuActivity.this,PedidosActivity.class));
            finish();
        });
        menu2.setOnClickListener(view -> {
            startActivity(new Intent(MenuActivity.this, LocalesActivity.class));
            finish();
        });
        menu3.setOnClickListener(view -> {
            startActivity(new Intent(MenuActivity.this,OfertasActivity.class));
            finish();
        });

    }
}