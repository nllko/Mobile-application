package com.example.mobile_application;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    URL loginUrl = new URL("https://engine.free.beeceptor.com/api/login");
    private TextView username;
    private TextView password;
    private MaterialButton loginButton;

    public LoginActivity() throws MalformedURLException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (TextView) findViewById(R.id.username);
        password = (TextView) findViewById(R.id.password);
        loginButton = (MaterialButton) findViewById(R.id.login_btn);

        loginButton.setOnClickListener(view -> {
            String inputUsername = username.getText().toString();
            String inputPassword = password.getText().toString();

            if (inputUsername.isEmpty() || inputPassword.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please enter all the details correctly", Toast.LENGTH_SHORT).show();
            } else {
                sendLoginData(inputUsername, inputPassword);
            }
        });
    }


    private void sendLoginData(String username, String password) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        service.execute(() -> {
            Integer responseCode = null;
            try {
                HttpURLConnection connection = (HttpURLConnection) loginUrl.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("username",username);
                connection.setRequestProperty("password",password);
                connection.connect();
                responseCode = connection.getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Integer finalResponseCode = responseCode;
            handler.post(() -> {
                if (finalResponseCode == HttpURLConnection.HTTP_OK) {
                    Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT).show();
                }
                if (finalResponseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    Toast.makeText(getApplicationContext(), "Login unsuccessful", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}