package com.example.pauloleonrosa.appcontente.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.pauloleonrosa.appcontente.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.AppConfig;
import app.AppController;

/**
 * Created by pauloleonrosa on 14/12/16.
 */
public class DetalheEntregaFragment extends Fragment
{

    ImageButton eventBtn, userBtn, itensBtn;
    Button finalizarEntrega;
    TextView ocorrenciaText;
    LocationManager locationManager;
    LocationListener locationListener;
    private ProgressDialog pDialog;
    int ocorrencia_global = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.detalhe_entrega, container, false);

        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        eventBtn = (ImageButton) rootView.findViewById(R.id.events);
        userBtn = (ImageButton) rootView.findViewById(R.id.user);
        itensBtn = (ImageButton) rootView.findViewById(R.id.itens);
        finalizarEntrega =(Button) rootView.findViewById(R.id.btn_finish);
        ocorrenciaText = (TextView) rootView.findViewById(R.id.textOcorrencia);




        String tag_string_req = "req_montagem";

        pDialog.setMessage("recebendo montagem");
        showDialog();

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST,
                AppConfig.URL_MONTAGEM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
                Log.d("TAG", "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {

                        String logradouro = jObj.getString("logradouro");
                        Log.d("TEste", logradouro);

                        String nomeCliente = jObj.getString("nomeCliente");


                        String telefone = jObj.getString("telefone");


;
                        setlist(logradouro, nomeCliente, telefone);


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
                Log.e("ERRORR", "Login Error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        "Ops! Login ou Senha incorretos", Toast.LENGTH_LONG)
                        .show();
                hideDialog();
            }
        })
        {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("fidmontagem", ((AppController) getActivity().getApplication()).getIdMontagemGlobal()+""); //Add the data you'd like to send to the server.

                return MyData;
            }
        };

        AppController.getInstance().addToRequestQueue(MyStringRequest, tag_string_req);





        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("FUNFFFFFFAAAAAA","\n " + location.getLongitude() + " " + location.getLatitude());
                //ENVIAR ESTA PORRA PARA O BANCO DE DADOS
                final String lat = ""+location.getLatitude();
                final String lng = ""+location.getLongitude();

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


        return rootView;

    }

    public void setlist(String logradouro, String nomeCliente, String telefone)
    {

        final String[] user = {nomeCliente, logradouro, telefone};
        final String[] lista = {"Base Lopas 0.90 C/VIDRO","QUADRO LOPAS C/ESPELHO","CADEIRA LOPAS TECIDO SUEDE ANIMALE BEGE"," AEREO MOB HORIZONTAL 1P BRACNCO SAL"};
        final String[] ocorrencia = getResources().getStringArray(R.array.ocorrencias
        );



        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
        userBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.detalhe_cliente)
                        .setItems(user, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                            }
                        });
                builder.create();
                builder.show();
            }
        });

        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
        itensBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.detalhe_itens)
                        .setItems(lista, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                            }
                        });
                builder.create();
                builder.show();
            }
        });




        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
        eventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.ocorrencia)
                        .setItems(R.array.ocorrencias, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        //cliente não estava em casa
                                        Toast.makeText(getActivity().getApplicationContext(),
                                                "Você definiu a Ocorrência:\n"+ ocorrencia[0], Toast.LENGTH_LONG)
                                                .show();
                                        ocorrencia_global = 1;
                                        ocorrenciaText.setText("Ocorrência Atual:\n" + ocorrencia[0]);
                                        break;
                                    case 1:
                                        //Endereço errado
                                        Toast.makeText(getActivity().getApplicationContext(),
                                                "Você definiu a Ocorrência:\n"+ ocorrencia[1], Toast.LENGTH_LONG)
                                                .show();
                                        ocorrencia_global = 2;
                                        ocorrenciaText.setText("Ocorrência Atual:\n" + ocorrencia[1]);


                                        break;
                                    case 2:
                                        //Produto Errado
                                        Toast.makeText(getActivity().getApplicationContext(),
                                                "Você definiu a Ocorrência:\n"+ ocorrencia[2], Toast.LENGTH_LONG)
                                                .show();
                                        ocorrencia_global = 3;
                                        ocorrenciaText.setText("Ocorrência Atual:\n" + ocorrencia[2]);

                                        break;
                                    case 3:
                                        //Problema de montagem
                                        Toast.makeText(getActivity().getApplicationContext(),
                                                "Você definiu a Ocorrência:\n"+ ocorrencia[3], Toast.LENGTH_LONG)
                                                .show();
                                        ocorrencia_global = 4;
                                        ocorrenciaText.setText("Ocorrência Atual:\n" + ocorrencia[3]);



                                        break;
                                }
                            }
                        });
                builder.create();
                builder.show();
            }
        });


        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
        finalizarEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalizarEntrega();
            }
        });

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }



    public void finalizarEntrega()
    {
        String tag_string_req = "finalizar_entrega";

        pDialog.setMessage("finalizando entrega");
        showDialog();

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST,
                AppConfig.URL_FINALIZAR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
                Log.d("TAG", "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {

                        Toast.makeText(getActivity().getApplicationContext(),
                                "Entrega / Montagem Finalizada com sucesso", Toast.LENGTH_LONG).show();


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
                Log.e("ERRORR", "Login Error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        "ERRO", Toast.LENGTH_LONG)
                        .show();
                hideDialog();
            }
        })
        {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("fidmontagem", ((AppController) getActivity().getApplication()).getIdMontagemGlobal()+""); //Add the data you'd like to send to the server.
                MyData.put("focorrencia", ocorrencia_global+"");
                ocorrencia_global=0;
                ocorrenciaText.setText("Sem Ocorrências!");

                return MyData;
            }
        };

        AppController.getInstance().addToRequestQueue(MyStringRequest, tag_string_req);

        //zerando ocorrencia


    }




}

