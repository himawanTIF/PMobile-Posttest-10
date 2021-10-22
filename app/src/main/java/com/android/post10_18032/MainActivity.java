package com.android.post10_18032;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText inputnim;
    private EditText inputnama;
    private EditText inputkelas;
    private EditText inputprodi;

    private String nim;
    private String nama;
    private String kelas;
    private String prodi;

    private Button tambah_data;
    private Button daftar_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputnim = (EditText) findViewById(R.id.NIM);
        inputnama = (EditText) findViewById(R.id.Nama);
        inputkelas = (EditText) findViewById(R.id.Kelas);
        inputprodi = (EditText) findViewById(R.id.Prodi);

        tambah_data = (Button) findViewById(R.id.add_data);
        daftar_data = (Button) findViewById(R.id.list_data);

        tambah_data.setOnClickListener(this);
        daftar_data.setOnClickListener(this);
    }

    private void add_mhs() {

        nim = inputnim.getText().toString().trim();
        nama = inputnama.getText().toString().trim();
        kelas = inputkelas.getText().toString().trim();
        prodi = inputprodi.getText().toString().trim();

        class addmhs extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Menambahkan...", "Tunggu...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put(WebParser.key_bio_nim, nim);
                params.put(WebParser.key_bio_nama, nama);
                params.put(WebParser.key_bio_kelas, kelas);
                params.put(WebParser.key_bio_prodi, prodi);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(WebParser.bio_tambah, params);
                return res;
            }
        }

        addmhs am = new addmhs();
        am.execute();
    }

    @Override
    public void onClick(View v) {
        if (v == tambah_data) {
            add_mhs();
        }

        if (v == daftar_data) {
            startActivity(new Intent(this, ListView.class));
        }
    }

}