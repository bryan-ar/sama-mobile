package com.android.sama;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {

    private EditText etNombre,etApellidoP,etApellidoM,etEmail,etDocumento,etPassword,etConfirmarPassword;
    Button btRegistrar;
    ProgressBar progressBar2;
    ImageView camera;
    int PERMISSION_ID = 44;
    private ImageView imageView;
    private String encodedImage1 = "";
    private byte[] byteArray;
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etNombre = findViewById(R.id.etNombre);
        etApellidoP = findViewById(R.id.etApellidoP);
        etApellidoM = findViewById(R.id.etApellidoM);
        etEmail = findViewById(R.id.etEmail);
        etDocumento = findViewById(R.id.etDocumento);
        etPassword = findViewById(R.id.etPassword);
        etConfirmarPassword = findViewById(R.id.etConfirmarPassword);
        progressBar2 = findViewById(R.id.progressBar2);
        camera = findViewById(R.id.camera);
        imageView = findViewById(R.id.roundedimage);

        if (!checkPermissions()) {
            requestPermissions();
        }

        camera.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            someActivityResultLauncher.launch(intent);
        });

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        btRegistrar = findViewById(R.id.btRegistrar);
        AppCompatImageView ivBack = findViewById(R.id.ivBack);

        btRegistrar.setOnClickListener(view -> {
            try {
                btRegistrar.setEnabled(false);
                register();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        ivBack.setOnClickListener(view -> finish());
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Bitmap bmp = (Bitmap) result.getData().getExtras().get("data");
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byteArray = stream.toByteArray();
                        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                        imageView.setImageBitmap(bitmap);
                        encodedImage1 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    }
                }
            });

    private void uploadImage() {
        if (byteArray != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image

            ref.putBytes(byteArray)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int) progress + "%");
                                }
                            });
        }
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.CAMERA}, PERMISSION_ID);
    }


    private void register() throws JSONException {

        progressBar2.setVisibility(View.VISIBLE);
        btRegistrar.setEnabled(false);

        String url = "http://10.0.2.2:52496/api/usuario/registrar";

        //Validaciones!!!

        JSONObject jsonParams = new JSONObject();
        jsonParams.put("email", etEmail.getText().toString());
        jsonParams.put("password", etPassword.getText().toString());
        jsonParams.put("confirm_password", etConfirmarPassword.getText().toString());
        jsonParams.put("nombres", etNombre.getText().toString());
        jsonParams.put("apellido_paterno", etApellidoP.getText().toString());
        jsonParams.put("apellido_materno", etApellidoM.getText().toString());
        jsonParams.put("documento_identidad", etDocumento.getText().toString());

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
                                uploadImage();

                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                finish();

                                Toast.makeText(RegisterActivity.this,"Usuario Registrado", Toast.LENGTH_SHORT).show();
                            }else{
                                progressBar2.setVisibility(View.GONE);
                                btRegistrar.setEnabled(true);
                                Toast.makeText(RegisterActivity.this,jsonResponse.getString("errorMessage"),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            progressBar2.setVisibility(View.GONE);
                            btRegistrar.setEnabled(true);
                            Toast.makeText(RegisterActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        progressBar2.setVisibility(View.GONE);
                        btRegistrar.setEnabled(true);
                        Toast.makeText(RegisterActivity.this,parseVolleyError(error),Toast.LENGTH_SHORT).show();
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