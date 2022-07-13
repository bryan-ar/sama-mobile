package com.android.sama.modals;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.sama.PedidosActivity;
import com.android.sama.R;
import com.android.sama.models.PedidoModel;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class CalificarModal extends DialogFragment {

    private int idPedido;
    RatingBar ratingBar;

    public static CalificarModal newInstance(int idPedido) {
        CalificarModal fragment = new CalificarModal();
        fragment.idPedido = idPedido;
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(true);
        return inflater.inflate(R.layout.fragment_calificacion, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        ImageView ivCancel = view.findViewById(R.id.ivCancel);
        ivCancel.setOnClickListener(view1 -> getDialog().dismiss());

        ratingBar = view.findViewById(R.id.ratingBar);

        Button btnFinalizar = view.findViewById(R.id.btnFinalizar);
        btnFinalizar.setOnClickListener(view1 -> {
            try {
                calificar();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    private void calificar() throws JSONException {
        String url = "http://10.0.2.2:52496/api/pedido/calificar";
        Log.i("======>", url);

        Gson gson = new Gson();
        PedidoModel pedido = new PedidoModel();
        pedido.setId(idPedido);
        pedido.setCalificacion(ratingBar.getRating());
        pedido.setEstado(2);

        String jsonPedido = gson.toJson(pedido);

        JSONObject jsonParams = new JSONObject(jsonPedido);

        Log.i("======>", jsonParams.toString());

        JsonObjectRequest
                jsonObjectRequest
                = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                jsonParams,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {

                        JSONObject jsonResponse = (JSONObject) response;

                        try {
                            if(jsonResponse.getString("isSuccess") == "true"){
                                Toast.makeText(getActivity(), "Gracias por calificar :)", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getActivity(), PedidosActivity.class);
                                getActivity().finish();
                                startActivity(i);
                                getDialog().dismiss();
                            }else{
                                Toast.makeText(getActivity(), "Revise la info que envía", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getActivity(), PedidosActivity.class);
                                getActivity().finish();
                                startActivity(i);
                                getDialog().dismiss();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), "Error al obtener respuesta", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getActivity(), PedidosActivity.class);
                            getActivity().finish();
                            startActivity(i);
                            getDialog().dismiss();
                            e.printStackTrace();
                        }
                    }
                },
                error -> {
                    Toast.makeText(getActivity(), "Error. Inténtelo más tarde", Toast.LENGTH_SHORT).show();
                    getDialog().dismiss();
                });

        // Adding request to request queue
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjectRequest);
    }
}
