package com.example.pauloleonrosa.appcontente;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by pauloleonrosa on 14/12/16.
 */
public class DetalheEntregaActivity extends AppCompatActivity {

    ImageButton pointBtn;
    Button finalizarEntrega;
    LocationManager locationManager;
    LocationListener locationListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalhe_entrega);
        pointBtn = (ImageButton) findViewById(R.id.events);
        finalizarEntrega =(Button) findViewById(R.id.btn_finish);


        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
        pointBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setTitle(R.string.ocorrencia)
                        .setItems(R.array.ocorrencias, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                            }
                        });
                builder.create();
                builder.show();
            }
        });


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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

    }


}
