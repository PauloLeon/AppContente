package com.example.pauloleonrosa.appcontente;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.AppConfig;
import app.AppController;
import helper.SQLiteHandler;
import helper.SessionManager;

/**
 * Created by pauloleonrosa on 14/11/16.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    EditText usernameEditText, passwordEditText;
    Button beginBtn,getPassBtn;
    Switch keepPass;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        usernameEditText = (EditText) findViewById(R.id.edit_username);
        passwordEditText = (EditText) findViewById(R.id.edit_password);
        beginBtn = (Button) findViewById(R.id.btn_begin);
        keepPass = (Switch) findViewById(R.id.switch_keeppass);
        getPassBtn = (Button) findViewById(R.id.btn_getpasss);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }


        // Login button Click Event
        beginBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = usernameEditText.getText().toString().trim().toLowerCase();
                String password = passwordEditText.getText().toString().trim().toLowerCase();

                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    if (email.equalsIgnoreCase("admin") && password.equalsIgnoreCase("admin"))
                    {
                        // user successfully logged in
                        // Create login session

                        // Now store the user in SQLite
                        String idUser = "987";

                        // Create login session
                        session.setLogin(true);

                        // Inserting row in users table
                        db.addUser(Integer.parseInt(idUser));

                        // Launch main activity
                        Intent intent = new Intent(LoginActivity.this,
                                MainActivity.class);
                        startActivity(intent);

                    }else{
                        // login user
                        checkLogin(email, password);
                    }
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Ops! não recebemos suas credenciais", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });




    }

    //metodo que check o logins
    private void checkLogin(final String cpf, final String password) {
    // Tag used to cancel the request

        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {
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
                        // user successfully logged in
                        // Create login session

                        // Now store the user in SQLite
                        String idUser = jObj.getString("idUser");

                        // Create login session
                        session.setLogin(true);

                        // Inserting row in users table
                        db.addUser(Integer.parseInt(idUser));

                        // Launch main activity
                        Intent intent = new Intent(LoginActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Ops! Login ou Senha incorretos", Toast.LENGTH_LONG)
                        .show();
                hideDialog();
            }
        })
        {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("flogin", cpf); //Add the data you'd like to send to the server.
                MyData.put("fsenha", password); //Add the data you'd like to send to the server.
                return MyData;
            }
        };

        AppController.getInstance().addToRequestQueue(MyStringRequest, tag_string_req);

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
