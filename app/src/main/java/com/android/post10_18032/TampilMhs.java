package com.android.post10_18032;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class TampilMhs extends AppCompatActivity implements View.OnClickListener {

    private EditText editnim;
    private EditText editnama;
    private EditText editkelas;
    private EditText editprodi;

    private String nim;

    private Button update_data;
    private Button delete_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_mhs);

        Intent intent = getIntent();

        nim = intent.getStringExtra(WebParser.bio_nim);

        editnim = (EditText) findViewById(R.id.nimmhs);
        editnama = (EditText) findViewById(R.id.namamhs);
        editkelas = (EditText) findViewById(R.id.kelasmhs);
        editprodi = (EditText) findViewById(R.id.prodimhs);

        update_data = (Button) findViewById(R.id.update_data);
        delete_data = (Button) findViewById(R.id.delete_data);

        update_data.setOnClickListener(this);
        delete_data.setOnClickListener(this);

        editnim.setText(nim);

        add_mhs();
    }

    private void add_mhs() {
        class addmhs extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TampilMhs.this, "Fetching...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                tampil_mhs(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(WebParser.bio_id, nim);
                return s;
            }
        }
        addmhs am = new addmhs();
        am.execute();
    }

    private void tampil_mhs(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(WebParser.bio_array);
            JSONObject c = result.getJSONObject(0);
            String nama = c.getString(WebParser.tag_bio_nama);
            String kelas = c.getString(WebParser.tag_bio_kelas);
            String prodi = c.getString(WebParser.tag_bio_prodi);

            editnama.setText(nama);
            editkelas.setText(kelas);
            editprodi.setText(prodi);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void Update() {
        final String nama = editnama.getText().toString().trim();
        final String kelas = editkelas.getText().toString().trim();
        final String prodi = editprodi.getText().toString().trim();

        class update_data extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TampilMhs.this, "Updating...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(TampilMhs.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(WebParser.key_bio_nim, nim);
                hashMap.put(WebParser.key_bio_nama, nama);
                hashMap.put(WebParser.key_bio_kelas, kelas);
                hashMap.put(WebParser.key_bio_prodi, prodi);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(WebParser.bio_update, hashMap);

                return s;
            }
        }

        update_data ud = new update_data();
        ud.execute();
    }

    private void delete_data() {
        class delete_data extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TampilMhs.this, "Updating...", "Tunggu...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(TampilMhs.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(WebParser.bio_delete, nim);
                return s;
            }
        }

        delete_data dd = new delete_data();
        dd.execute();
    }

    private void Delete() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Hapus Data ?");

        alertDialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        delete_data();
                        startActivity(new Intent(TampilMhs.this, ListView.class));
                    }
                });

        alertDialogBuilder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v == update_data) {
            Update();
        }

        if (v == delete_data) {
            Delete();
        }
    }
}