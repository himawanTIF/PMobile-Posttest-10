package com.android.post10_18032;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Login extends AppCompatActivity {

    private EditText inputusername;
    private EditText inputpassword;

    private String username;
    private String password;

    private String user;
    private String pass;

    private String jsons;
    int userconf = 0;
    int passconf = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getJSON();
    }

    private void tampil_login() {
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(jsons);
            JSONArray result = jsonObject.getJSONArray(WebParser.login_array);

            JSONObject i = result.getJSONObject(0);
            user = i.getString(WebParser.username);
            pass = i.getString(WebParser.password);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Login.this, "Mengambil Data", "Mohon Tunggu...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                jsons = s;
                tampil_login();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String srq = rh.sendGetRequest(WebParser.login_check);
                return srq;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    public void login(View view) {
        inputusername = (EditText) findViewById(R.id.inputusername);
        inputpassword = (EditText) findViewById(R.id.inputpassword);

        username = inputusername.getText().toString();
        password = inputpassword.getText().toString();

        if (username.equals(user)) {
            Toast.makeText(this, "Username Benar", Toast.LENGTH_SHORT).show();
            userconf = 1;
        } else {
            Toast.makeText(this, "Username Salah", Toast.LENGTH_SHORT).show();
        }

        if (password.equals(pass)) {
            Toast.makeText(this, "Password Benar", Toast.LENGTH_SHORT).show();
            passconf = 1;
        } else {
            Toast.makeText(this, "Password Salah", Toast.LENGTH_SHORT).show();
        }

        if (username.equals(user) && password.equals(pass)) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
