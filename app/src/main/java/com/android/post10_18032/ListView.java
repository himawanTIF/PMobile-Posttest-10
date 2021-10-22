package com.android.post10_18032;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ListView extends AppCompatActivity implements android.widget.ListView.OnItemClickListener {

    private android.widget.ListView listview;

    private String JSON_STRING;
    private String nim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        listview = (android.widget.ListView) findViewById(R.id.listview);
        listview.setOnItemClickListener(this);
        getJSON();
    }

    private void tampil_mhs() {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(WebParser.bio_array);

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                nim = jo.getString(WebParser.tag_bio_nim);
                String nama = jo.getString(WebParser.tag_bio_nama);

                HashMap<String, String> mhs = new HashMap<>();
                mhs.put(WebParser.tag_bio_nim, nim);
                mhs.put(WebParser.tag_bio_nama, nama);
                list.add(mhs);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] from = {WebParser.tag_bio_nim, WebParser.tag_bio_nama};
        int[] to = {R.id.nimmhs, R.id.namamhs};

        ListAdapter adapter = new SimpleAdapter(
                ListView.this, list, R.layout.listmhs_prop, from, to);

        listview.setAdapter(adapter);
    }

    private void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ListView.this, "Mengambil Data", "Mohon Tunggu...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                tampil_mhs();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(WebParser.bio_smw);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, TampilMhs.class);
        HashMap<String, String> map = (HashMap) parent.getItemAtPosition(position);
        String empId = map.get(WebParser.tag_bio_nim).toString();
        intent.putExtra(WebParser.bio_nim, empId);
        startActivity(intent);
    }

    public void aa(View view) {
        Toast.makeText(this, nim, Toast.LENGTH_SHORT).show();
    }
}
