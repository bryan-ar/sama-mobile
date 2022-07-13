package com.android.sama;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.sama.adapters.OfertasAdapter;
import com.android.sama.dao.OfertaDAO;
import com.android.sama.models.OfertaModel;
import com.android.sama.utils.Constants;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

public class OfertasActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    OfertasAdapter ofertasAdapter;
    ArrayList<OfertaModel> listaOfertas;
    OfertaDAO ofertaDAO;
    TextView tv_nombres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ofertas);

        SharedPreferences myPrefs = this.getSharedPreferences(Constants.PrefsLogin, Context.MODE_PRIVATE);
        String nombres = myPrefs.getString("nombres",null);
        String apellido_paterno = myPrefs.getString("apellido_paterno",null);
        String apellido_materno = myPrefs.getString("apellido_materno",null);

        listaOfertas = new ArrayList<>();
        recyclerView = findViewById(R.id.rvOfertas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tv_nombres = findViewById(R.id.tv_nombres);
        tv_nombres.setText(nombres + " " + apellido_paterno + " " + apellido_materno);

        ofertaDAO = new OfertaDAO(this);
        listarOfertas();

        ImageView ivMenuOfertas = findViewById(R.id.ivMenuOfertas);
        ivMenuOfertas.setOnClickListener(view -> {
            startActivity(new Intent(OfertasActivity.this, MenuActivity.class));
            finish();
        });
    }

    private void listarOfertas() {

        String url = "http://10.0.2.2:52496/api/oferta/getofertas";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    Gson gson = new Gson();

                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        Log.i("======>", jsonResponse.toString());

                        if (jsonResponse.getString("isSuccess") == "true") {
                            OfertaModel[] ofertas = gson.fromJson(jsonResponse.getJSONArray("data").toString(),
                                    OfertaModel[].class);

                            ArrayList<OfertaModel> listaOfertas = new ArrayList<>(Arrays.asList(ofertas));

                            ofertasAdapter = new OfertasAdapter(OfertasActivity.this, listaOfertas);
                            recyclerView.setAdapter(ofertasAdapter);
                        } else {
                            Toast.makeText(OfertasActivity.this, jsonResponse.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(OfertasActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.i("======>", e.getMessage());
                    }
                },
                error -> Toast.makeText(OfertasActivity.this, parseVolleyError(error), Toast.LENGTH_SHORT).show()
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public String parseVolleyError(VolleyError error) {
        if (error.networkResponse != null && error.networkResponse.data != null) {
            try {
                String responseBody = new String(error.networkResponse.data, "utf-8");
                JSONObject data = new JSONObject(responseBody);
                JSONArray errors = data.getJSONArray("errors");
                JSONObject jsonMessage = errors.getJSONObject(0);
                String message = jsonMessage.getString("message");
                return message;
            } catch (JSONException e) {
            } catch (UnsupportedEncodingException unsupportedError) {
            }
        }

        return error.toString();
    }
}