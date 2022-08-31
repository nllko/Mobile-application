package com.example.mobile_application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.mobile_application.model.Sport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity {

    URL servicesUrl = new URL("https://engine.free.beeceptor.com/api/getServices");
    ArrayList<Sport> sports = new ArrayList<>();

    public MainActivity() throws MalformedURLException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        getServicesJson();
        CustomAdapter customAdapter = new CustomAdapter(MainActivity.this,sports);
        recyclerView.setAdapter(customAdapter);

    }

    private void getServicesJson(){
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            try {
                HttpURLConnection connection = (HttpURLConnection) servicesUrl.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                JSONArray jsonArray = new JSONArray(response.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    Sport sport = new Sport(object.getString("name"),object.getInt("id"));
                    sports.add(sport);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        });
    }
}