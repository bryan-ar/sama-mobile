package com.android.sama;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.sama.models.UsuarioModel;
import com.android.sama.sqlite.ManageOpenHelper;
import com.android.sama.utils.Constants;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    ManageOpenHelper dbConfig;
    ProgressBar progressBar2;
    Button loginButton;
    TextView tvRegister;
    String token;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences myPrefs = this.getSharedPreferences(Constants.PrefsLogin, Context.MODE_PRIVATE);
        String nombres = myPrefs.getString("nombres",null);

        if(nombres != null){
            startActivity(new Intent(LoginActivity.this, MenuActivity.class));
            finish();
            return;
        }

        dbConfig = new ManageOpenHelper(this);
        dbConfig.cleanBD();

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        progressBar2 = findViewById(R.id.progressBar2);

        loginButton = findViewById(R.id.login);
        tvRegister = findViewById(R.id.tvRegister);

        loginButton.setOnClickListener(v -> {
            try {
                login();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        tvRegister.setOnClickListener(view ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("FCM", "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    token = task.getResult();
                });
    }

    private void login() throws JSONException {

        tvRegister.setEnabled(false);
        loginButton.setEnabled(false);
        progressBar2.setVisibility(View.VISIBLE);

        String url = "http://10.0.2.2:52496/api/usuario/login";
        Log.i("======>", url);

        JSONObject jsonParams = new JSONObject();
        jsonParams.put("username", username.getText().toString());
        jsonParams.put("password", password.getText().toString());
        jsonParams.put("firebaseId", token);

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
                        Gson gson = new Gson();
                        JSONObject jsonResponse = (JSONObject) response;

                        try {
                            if(jsonResponse.getString("isSuccess") == "true"){
                                UsuarioModel objUsuario = gson.fromJson(jsonResponse.getJSONObject("data").toString(),
                                        UsuarioModel.class);

                                SharedPreferences sharedPref = getSharedPreferences(Constants.PrefsLogin, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("username", username.getText().toString());
                                editor.putString("password", password.getText().toString());
                                editor.putString("nombres", objUsuario.getNombres());
                                editor.putString("apellido_paterno", objUsuario.getApellido_paterno());
                                editor.putString("apellido_materno", objUsuario.getApellido_materno());
                                editor.putString("id_usuario", String.valueOf(objUsuario.getId()));
                                editor.commit();

                                startActivity(new Intent(LoginActivity.this, MenuActivity.class));
                                finish();

                                Toast.makeText(LoginActivity.this,"Bienvenido", Toast.LENGTH_SHORT).show();
                            }else{
                                tvRegister.setEnabled(true);
                                loginButton.setEnabled(true);
                                progressBar2.setVisibility(View.GONE);

                                Toast.makeText(LoginActivity.this,jsonResponse.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            tvRegister.setEnabled(true);
                            loginButton.setEnabled(true);
                            progressBar2.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        tvRegister.setEnabled(true);
                        loginButton.setEnabled(true);
                        progressBar2.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this,parseVolleyError(error), Toast.LENGTH_SHORT).show();
                    }
                });

        // Adding request to request queue
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

    }

    public String parseVolleyError(VolleyError error) {
        if(error.networkResponse != null && error.networkResponse.data != null){
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