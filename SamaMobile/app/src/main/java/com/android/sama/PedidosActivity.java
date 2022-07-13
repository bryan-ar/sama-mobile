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

import com.android.sama.adapters.PedidosAdapter;
import com.android.sama.dao.PedidoDAO;
import com.android.sama.models.PedidoModel;
import com.android.sama.utils.Constants;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

public class PedidosActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    PedidosAdapter pedidosAdapter;
    ArrayList<PedidoModel> listaPedidos;
    MaterialButton btnNuevoPedido;
    PedidoDAO pedidoDAO;
    private TextView tv_nombres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        listaPedidos = new ArrayList<>();

        recyclerView = findViewById(R.id.rvPedidos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnNuevoPedido = findViewById(R.id.btn_nuevo_pedido);

        SharedPreferences myPrefs = this.getSharedPreferences(Constants.PrefsLogin, Context.MODE_PRIVATE);
        String id_usuario = myPrefs.getString("id_usuario", null);

        String nombres = myPrefs.getString("nombres",null);
        String apellido_paterno = myPrefs.getString("apellido_paterno",null);
        String apellido_materno = myPrefs.getString("apellido_materno",null);

        tv_nombres = findViewById(R.id.tv_nombres);
        tv_nombres.setText(nombres + " " + apellido_paterno + " " + apellido_materno);

        pedidoDAO = new PedidoDAO(this);
        PedidoModel pedido = pedidoDAO.obtenerPedidoUsuario(Integer.parseInt(id_usuario));
        listarPedidos(id_usuario);

        if (pedido.getIdUsuario() > 0) {
            btnNuevoPedido.setText("Pedido en curso");
            btnNuevoPedido.setIcon(getDrawable(R.drawable.icon_edit));
        }

        ImageView ivMenuPedidos = findViewById(R.id.ivMenuPedidos);
        ivMenuPedidos.setOnClickListener(view -> {
            startActivity(new Intent(PedidosActivity.this, MenuActivity.class));
            finish();
        });


        btnNuevoPedido.setOnClickListener(view -> {
            startActivity(new Intent(PedidosActivity.this, NuevoPedidoActivity.class));
        });
    }

    private void listarPedidos(String id_usuario) {

        String url = "http://10.0.2.2:52496/api/pedido/getpedidos/" + id_usuario;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    Log.i("======>", jsonResponse.toString());

                    if (jsonResponse.getString("isSuccess") == "true") {
                        PedidoModel[] pedidos = gson.fromJson(jsonResponse.getJSONArray("data").toString(),
                                PedidoModel[].class);

                        ArrayList<PedidoModel> listaPedidos = new ArrayList<>(Arrays.asList(pedidos));

                        pedidosAdapter = new PedidosAdapter(PedidosActivity.this, listaPedidos);
                        recyclerView.setAdapter(pedidosAdapter);
                    } else {
                        Toast.makeText(PedidosActivity.this, jsonResponse.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(PedidosActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i("======>", e.getMessage());
                }
            }
        },
                error -> Toast.makeText(PedidosActivity.this, parseVolleyError(error), Toast.LENGTH_SHORT).show()
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