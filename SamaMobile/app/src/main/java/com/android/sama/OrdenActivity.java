package com.android.sama;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.sama.adapters.OrdenDetalleAdapter;
import com.android.sama.adapters.OrdenNuevaAdapter;
import com.android.sama.dao.OrdenDAO;
import com.android.sama.dao.PedidoDAO;
import com.android.sama.interfaces.DataTransferInterface;
import com.android.sama.models.PedidoModel;
import com.android.sama.models.PedidoOrdenModel;
import com.android.sama.utils.Constants;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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

public class OrdenActivity extends AppCompatActivity implements DataTransferInterface {

    private RecyclerView rvOrdenDetalle, rvOrdenNuevoPedido;
    private Button btnRegistrarPedido;
    private TextView tvTotalPagar;
    private int id_pedido;
    private OrdenDAO ordenDAO;
    private PedidoDAO pedidoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orden);

        rvOrdenDetalle = findViewById(R.id.rvOrdenDetalle);
        rvOrdenDetalle.setLayoutManager(new LinearLayoutManager(this));
        rvOrdenNuevoPedido = findViewById(R.id.rvOrdenNuevoPedido);
        rvOrdenNuevoPedido.setLayoutManager(new LinearLayoutManager(this));
        btnRegistrarPedido = findViewById(R.id.btn_registrar_pedido);
        tvTotalPagar = findViewById(R.id.tvTotalPagar);

        ordenDAO = new OrdenDAO(this);
        pedidoDAO = new PedidoDAO(this);

        int flag = getIntent().getIntExtra("nuevo_pedido", 1);
        id_pedido = getIntent().getIntExtra("id_pedido", 0);

        if(flag == 0){
            rvOrdenDetalle.setVisibility(View.VISIBLE);
            rvOrdenNuevoPedido.setVisibility(View.GONE);
            //cargar detalle de API
            listarOrden();
            //ocultar boton de pagar
            btnRegistrarPedido.setVisibility(View.GONE);
        } else{
            rvOrdenDetalle.setVisibility(View.GONE);
            rvOrdenNuevoPedido.setVisibility(View.VISIBLE);
            //cargar productos seleccionados en sqlite
            cargarOrdenDB();

            btnRegistrarPedido.setVisibility(View.VISIBLE);
        }

        btnRegistrarPedido.setOnClickListener(view ->
                startActivity(new Intent(OrdenActivity.this, PaymentActivity.class)));

        ImageView ivMenuPedidos = findViewById(R.id.ivMenuPedidos);
        ivMenuPedidos.setOnClickListener(view -> finish());

    }

    private void cargarOrdenDB() {
        ArrayList<PedidoOrdenModel> listaOrden = ordenDAO.obtenerOrden();

        OrdenNuevaAdapter ordenDetalleAdapter = new OrdenNuevaAdapter(OrdenActivity.this, listaOrden, this);
        rvOrdenNuevoPedido.setAdapter(ordenDetalleAdapter);

    }

    private void listarOrden(){
        String url = "http://10.0.2.2:52496/api/pedido/getpedido/" + id_pedido;

        StringRequest stringRequest= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    Log.i("======>", jsonResponse.toString());

                    if(jsonResponse.getString("isSuccess") == "true") {
                        PedidoModel pedido = gson.fromJson(jsonResponse.getJSONObject("data").toString(),
                                PedidoModel.class);

                        tvTotalPagar.setText("S/" + pedido.getMonto_total());

                        PedidoOrdenModel[] orden = gson.fromJson(jsonResponse.getJSONObject("data")
                                .getJSONArray("orden").toString(), PedidoOrdenModel[].class);

                        ArrayList<PedidoOrdenModel> listaOrden = new ArrayList<>(Arrays.asList(orden));

                        OrdenDetalleAdapter ordenDetalleAdapter = new OrdenDetalleAdapter(OrdenActivity.this, listaOrden);
                        rvOrdenDetalle.setAdapter(ordenDetalleAdapter);
                    }
                    else {
                        Toast.makeText(OrdenActivity.this, jsonResponse.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Log.i("======>", e.getMessage());
                }
            }
        },
                error -> Toast.makeText(OrdenActivity.this, parseVolleyError(error), Toast.LENGTH_SHORT).show()
        );
        RequestQueue requestQueue= Volley.newRequestQueue(this);
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

    @Override
    public void actualizarTotalPagar() {
        SharedPreferences myPrefs = this.getSharedPreferences(Constants.PrefsLogin, Context.MODE_PRIVATE);
        int id_usuario = Integer.parseInt(myPrefs.getString("id_usuario","0"));

        PedidoModel pedido = pedidoDAO.obtenerPedidoUsuario(id_usuario);

        if(pedido.getIdUsuario() > 0){
            tvTotalPagar.setText("" + pedido.getMonto_total());
        }
    }
}