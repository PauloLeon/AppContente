package com.example.pauloleonrosa.appcontente.Fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.pauloleonrosa.appcontente.MainActivity;
import com.example.pauloleonrosa.appcontente.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.AppConfig;
import app.AppController;

/**
 * Created by pauloleonrosa on 13/12/16.
 */
public class PontoMeioIdaFragment extends Fragment {

    Button pointBtn;
    LocationManager locationManager;
    LocationListener locationListener;
    TextView ateProxima,hora;
    private ProgressDialog pDialog;
    String lat;
    String lng;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.ponto_meio_ida, container, false);
        pointBtn = (Button) rootView.findViewById(R.id.btn_gps);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("FUNFFFFFFAAAAAA","\n " + location.getLongitude() + " " + location.getLatitude());
                //ENVIAR ESTA PORRA PARA O BANCO DE DADOS
                lat = ""+location.getLatitude();
                lng = ""+location.getLongitude();


            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        configureButton();

        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
        pointBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //noinspection MissingPermission
                locationManager.requestLocationUpdates("gps", 0, 0, locationListener);
                // Tag used to cancel the request
                String tag_string_req = "req_location";

                pDialog.setMessage("send location");
                showDialog();

                StringRequest MyStringRequest = new StringRequest(Request.Method.POST,
                        AppConfig.URL_LOGIN, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //This code is executed if the server responds, whether or not the response contains data.
                        //The String 'response' contains the server's response.
                        Log.d("TAG", "Location Response: " + response.toString());
                        hideDialog();

                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean("error");

                            // Check for error node in json
                            if (!error) {
                                //enviando para a proxima tela
                                sendToMainActivity();

                            } else {
                                // Error in login. Get the error message
                                String errorMsg = jObj.getString("error");
                                Toast.makeText(getActivity().getApplicationContext(),
                                        errorMsg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(getActivity().getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", "Login Error: " + error.getMessage());
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Ops! Login ou Senha incorretos", Toast.LENGTH_LONG)
                                .show();
                        hideDialog();
                    }
                })
                {
                    protected Map<String, String> getParams() {
                        Map<String, String> MyData = new HashMap<String, String>();
                        MyData.put("flat", lat); //Add the data you'd like to send to the server.
                        MyData.put("flng", lng); //Add the data you'd like to send to the server.
                        MyData.put("fdescricao", "1"); //Add the data you'd like to send to the server.
                        return MyData;
                    }
                };

                AppController.getInstance().addToRequestQueue(MyStringRequest, tag_string_req);

            }
        });


        return rootView;

    }


    private void configureButton() {
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }

    }

    public void sendToMainActivity(){
        Intent main = new Intent(getActivity(), MainActivity.class);
        startActivity(main);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                configureButton();
                break;
            default:
                break;
        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}


