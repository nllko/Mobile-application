package com.example.mobile_application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

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
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    URL servicesUrl = new URL("https://engine.free.beeceptor.com/api/getServices");

    public MainActivity() throws MalformedURLException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        try {
            ArrayList<Sport> sports = convertJsonToSportArray(getServicesJson());
            CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(),sports);
            recyclerView.setAdapter(customAdapter);
        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }

    }

    private JSONArray getServicesJson() throws ExecutionException, InterruptedException {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<JSONArray> jsonArray = service.submit(() -> {
            String json = null;
            try {
                HttpURLConnection connection = (HttpURLConnection) servicesUrl.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                json = response.toString();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return new JSONArray(json);
        });
        return jsonArray.get();
    }

    private ArrayList<Sport> convertJsonToSportArray(JSONArray jsonArray) throws JSONException {
        ArrayList<Sport> sports = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            Sport sport = new Sport(object.getString("name"), object.getInt("id"));
            sports.add(sport);
        }
        return sports;
    }
}