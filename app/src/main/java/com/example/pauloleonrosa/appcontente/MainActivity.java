package com.example.pauloleonrosa.appcontente;

import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.pauloleonrosa.appcontente.Fragment.CaptureSignatureFragment;
import com.example.pauloleonrosa.appcontente.Fragment.GmapFragment;
import com.example.pauloleonrosa.appcontente.Fragment.LocationFragment;
import com.example.pauloleonrosa.appcontente.Fragment.PontoFinalFragment;
import com.example.pauloleonrosa.appcontente.Fragment.PontoInicialFragment;
import com.example.pauloleonrosa.appcontente.Fragment.PontoMeioIdaFragment;
import com.example.pauloleonrosa.appcontente.Fragment.PontoMeioVoltaFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView =  null;
    Toolbar toolbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

       /* MainFragment mainFragment = new MainFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, mainFragment);
        fragmentTransaction.commit();*/

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q=av.duque de caxias 1735+belem"));
                startActivity(intent);

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
           }
        });
        fab.setVisibility(View.INVISIBLE);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.fragment_container, new com.example.pauloleonrosa.appcontente.Fragment.MainFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fm = getFragmentManager();


        if (id == R.id.nav_home) {
           // fm.beginTransaction().replace(R.id.fragment_container, new MainFragment()).commit();

        } else if (id == R.id.nav_route) {
            LocationFragment locationFragment = new LocationFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, locationFragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_maps) {
            fm.beginTransaction().replace(R.id.fragment_container, new GmapFragment()).commit();

        } else if (id == R.id.nav_signature) {
            fm.beginTransaction().replace(R.id.fragment_container, new CaptureSignatureFragment()).commit();


        }  else if (id == R.id.nav_point_one) {
            fm.beginTransaction().replace(R.id.fragment_container, new PontoInicialFragment()).commit();


        }  else if (id == R.id.nav_point_two) {
            fm.beginTransaction().replace(R.id.fragment_container, new PontoMeioIdaFragment()).commit();


        }  else if (id == R.id.nav_point_three) {
            fm.beginTransaction().replace(R.id.fragment_container, new PontoMeioVoltaFragment()).commit();


        }  else if (id == R.id.nav_point_four) {
            fm.beginTransaction().replace(R.id.fragment_container, new PontoFinalFragment()).commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
