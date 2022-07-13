package com.android.sama;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.sama.dao.OrdenDAO;
import com.android.sama.dao.PagoDAO;
import com.android.sama.dao.PedidoDAO;
import com.android.sama.models.PedidoModel;
import com.android.sama.models.PedidoPagoModel;
import com.android.sama.sqlite.ManageOpenHelper;
import com.android.sama.utils.Constants;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fevziomurtekin.payview.Payview;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class PaymentActivity extends AppCompatActivity {

    PedidoDAO pedidoDAO;
    OrdenDAO ordenDAO;
    PagoDAO pagoDAO;
    ManageOpenHelper db;

    TextInputEditText tev_card_name, tev_card_no, tev_card_year, tev_card_month, tev_card_cv;
    Button btn_pay;
    ProgressBar progressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        db = new ManageOpenHelper(this);
        pedidoDAO = new PedidoDAO(this);
        ordenDAO = new OrdenDAO(this);
        pagoDAO = new PagoDAO(this);

        tev_card_name = findViewById(com.fevziomurtekin.payview.R.id.tev_card_name);
        tev_card_no = findViewById(com.fevziomurtekin.payview.R.id.tev_card_no);
        tev_card_year = findViewById(com.fevziomurtekin.payview.R.id.tev_card_year);
        tev_card_month = findViewById(com.fevziomurtekin.payview.R.id.tev_card_month);
        tev_card_cv = findViewById(com.fevziomurtekin.payview.R.id.tev_card_cv);
        btn_pay = findViewById(com.fevziomurtekin.payview.R.id.btn_pay);
        progressBar2 = findViewById(R.id.progressBar2);

        ImageView ivAtrasPayment = findViewById(R.id.ivAtrasPayment);
        ivAtrasPayment.setOnClickListener(view -> finish());

        Payview payview = findViewById(R.id.payview);

        // on below line we are setting pay on listener for our card.
        payview.setPayOnclickListener(view -> {
            try {
                pagoDAO.insertarPago(new PedidoPagoModel(
                        0, 0, tev_card_no.getText().toString(),
                        tev_card_name.getText().toString(),
                        tev_card_month.getText().toString() + "/" + tev_card_year.getText().toString(),
                        tev_card_cv.getText().toString(),
                        1
                ));

                pagar();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void pagar() throws JSONException {
        btn_pay.setEnabled(false);
        progressBar2.setVisibility(View.VISIBLE);

        SharedPreferences myPrefs = getSharedPreferences(Constants.PrefsLogin, Context.MODE_PRIVATE);
        int id_usuario = Integer.parseInt(myPrefs.getString("id_usuario","0"));

        String url = "http://10.0.2.2:52496/api/pedido/registrar";
        Log.i("======>", url);

        Gson gson = new Gson();
        PedidoModel pedido = pedidoDAO.obtenerPedidoUsuario(id_usuario);
        pedido.orden = ordenDAO.obtenerOrden();
        pedido.pago = pagoDAO.obtenerPago();

        //pedido cambia a pagado
        pedido.setEstado(0);

        String jsonPedido = gson.toJson(pedido);

        JSONObject jsonParams = new JSONObject(jsonPedido);

        Log.i("======>", jsonParams.toString());

        JsonObjectRequest
                jsonObjectRequest
                = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonParams,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {

                        JSONObject jsonResponse = (JSONObject) response;

                        try {
                            if(jsonResponse.getString("isSuccess") == "true"){
                                db.cleanBD();
                                startActivity(new Intent(PaymentActivity.this, MenuActivity.class));
                                finish();

                                Toast.makeText(PaymentActivity.this,"Pedido registrado", Toast.LENGTH_SHORT).show();
                            }else{
                                btn_pay.setEnabled(true);
                                progressBar2.setVisibility(View.GONE);
                                Toast.makeText(PaymentActivity.this,jsonResponse.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            btn_pay.setEnabled(true);
                            progressBar2.setVisibility(View.GONE);
                            Toast.makeText(PaymentActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                error -> {
                    btn_pay.setEnabled(true);
                    progressBar2.setVisibility(View.GONE);
                    Toast.makeText(PaymentActivity.this,parseVolleyError(error), Toast.LENGTH_SHORT).show();
                });

        // Adding request to request queue
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
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