package com.example.pauloleonrosa.appcontente.Fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.pauloleonrosa.appcontente.ListView.Endereco;
import com.example.pauloleonrosa.appcontente.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.AppConfig;
import app.AppController;
import helper.SQLiteHandler;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment {


    public LocationFragment() {
    }

    private ProgressDialog pDialog;
    private SQLiteHandler db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getActivity().getApplicationContext());

        View rootView = inflater.inflate(R.layout.fragment_location, container, false);

        final ListView listadeEnderecos = (ListView) rootView.findViewById(R.id.listAddress);

        final String idUser = db.getUserDetails().get("id");


        final List<Endereco> enderecos = null;

        String tag_string_req = "req_location";

        pDialog.setMessage("Buscando Endereços ...");
        showDialog();

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOCATION, new Response.Listener<String>() {
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


                        JSONArray jObjInside = new JSONArray(jObj.getString("rows"));

                        List<Endereco> enderecosJSON = new ArrayList<Endereco>();
                        for (int i = 0; i < jObjInside.length(); i++) {
                            int idMontagem = Integer.parseInt(jObjInside.getJSONObject(i).getString("idMontagens"));
                            String logradouro = jObjInside.getJSONObject(i).getString("logradouro");
                            int prioridade = Integer.parseInt(jObjInside.getJSONObject(i).getString("prioridade"));
                            enderecosJSON.add(new Endereco(logradouro, prioridade, idMontagem));
                        }

                        final List<Endereco> enderecos = enderecosJSON;

                        AdapterEnderecosPersonalizado adapter =
                                new AdapterEnderecosPersonalizado(enderecos, getActivity());

                        listadeEnderecos.setAdapter(adapter);


                        listadeEnderecos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                final int pos = position;
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle(R.string.escolha_acao)
                                        .setItems(R.array.locationarray, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // The 'which' argument contains the index position
                                                // of the selected item
                                                if(which==1){

                                                    DetalheEntregaFragment detalheEntregaFragment = new DetalheEntregaFragment();
                                                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                                    fragmentTransaction.replace(R.id.fragment_container, detalheEntregaFragment);
                                                    //colocando o idMontagemCorrente
                                                    ((AppController) getActivity().getApplication()).setIdMontagemGlobal(enderecos.get(pos).getIdMontagem());
                                                    fragmentTransaction.commit();
                                                }else{
                                                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                                            Uri.parse("google.navigation:q="+enderecos.get(pos).getLogradouro()));
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                                builder.create();
                                builder.show();
                            }
                        });

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
                Log.e("ERROR ADRESS", "Login Error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        "ERRO", Toast.LENGTH_LONG)
                        .show();
                hideDialog();
            }
        })
        {
            protected Map<String, String> getParams() {

                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("fuser", idUser); //Add the data you'd like to send to the server.
                return MyData;
            }
        };

        AppController.getInstance().addToRequestQueue(MyStringRequest, tag_string_req);



        return rootView;
    }


    public class AdapterEnderecosPersonalizado extends BaseAdapter {

        private final List<Endereco> enderecos;
        private final Activity activity;

        public AdapterEnderecosPersonalizado(List<Endereco> enderecos, Activity activity) {
            this.enderecos = enderecos;
            this.activity = activity;
        }

        @Override
        public int getCount() {
            return enderecos.size();
        }

        @Override
        public Object getItem(int position) {
            return enderecos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = activity.getLayoutInflater()
                    .inflate(R.layout.list_enderecos_customizada, parent, false);
            Endereco endereco = enderecos.get(position);

            TextView nome = (TextView)
                    view.findViewById(R.id.lista_curso_personalizada_nome);
            TextView descricao = (TextView)
                    view.findViewById(R.id.lista_curso_personalizada_descricao);
        //    ImageView imagem = (ImageView)
        //            view.findViewById(R.id.lista_curso_personalizada_imagem);

            //populando as Views
            nome.setText(endereco.getLogradouro());
            descricao.setText("Prioridade: "+endereco.getPrioridade());
        //  imagem.setImageResource(R.drawable.ic_menu_directions);
            return view;
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
