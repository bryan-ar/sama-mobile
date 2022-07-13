package com.android.sama;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.android.sama.adapters.ProductosAdapter;
import com.android.sama.dao.ProductoDAO;
import com.android.sama.models.ProductoModel;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class NuevoPedidoActivity extends AppCompatActivity {

    private RecyclerView rvProductos;
    private ArrayList<ProductoModel> productos;
    private ProductoDAO productoDAO;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_pedido);

        rvProductos = findViewById(R.id.rvProductos);
        tabLayout = findViewById(R.id.tabLayout);

        productoDAO = new ProductoDAO(this, true);
        cargarProductos();

        Button btnVerCarrito = findViewById(R.id.btnVerCarrito);
        btnVerCarrito.setOnClickListener(view ->
                startActivity(new Intent(NuevoPedidoActivity.this, OrdenActivity.class)
                        .putExtra("nuevo_pedido", 1).putExtra("id_pedido", 0)));

        ImageView ivMenuPedidos = findViewById(R.id.ivMenuPedidos);
        ivMenuPedidos.setOnClickListener(view -> finish());

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position){
                    case 0:
                        productos = new ArrayList<>();
                        productos = productoDAO.listarProductos(1);
                        break;
                    case 1:
                        productos = new ArrayList<>();
                        productos = productoDAO.listarProductos(2);
                        break;
                    case 2:
                        productos = new ArrayList<>();
                        productos = productoDAO.listarProductos(3);
                        break;
                }
                ProductosAdapter productosAdapter = new ProductosAdapter(productos,getBaseContext());
                rvProductos.setAdapter(productosAdapter);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void cargarProductos(){
        String url = "http://10.0.2.2:52496/api/pedido/getproductos";

        StringRequest stringRequest= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    Log.i("======>", jsonResponse.toString());

                    if(jsonResponse.getString("isSuccess") == "true") {
                        ProductoModel[] productos = gson.fromJson(jsonResponse.getJSONArray("data").toString(),
                                ProductoModel[].class);

                        ArrayList<ProductoModel> listaProductos = new ArrayList<>(Arrays.asList(productos));

                        for (ProductoModel x : listaProductos) {
                            productoDAO.insertarProducto(x);
                        }

                        cargarProductosBD();
                    }

                } catch (JSONException e) {
                    Log.i("======>", e.getMessage());
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("======>", error.toString());
                    }
                }
        );
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void cargarProductosBD() {
        // created new array list..
        productos = new ArrayList<>();
        productos = productoDAO.listarProductos(1);

        // added data from arraylist to adapter class.
        ProductosAdapter adapter = new ProductosAdapter(productos,this);

        // setting grid layout manager to implement grid view.
        // in this method '2' represents number of columns to be displayed in grid view.
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);

        // at last set adapter to recycler view.
        rvProductos.setLayoutManager(layoutManager);
        rvProductos.setAdapter(adapter);
    }
}