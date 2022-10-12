package com.example.mobile_application;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    ArrayList<Sport> sports;
    int[] sportsImages = {R.drawable.ic_baseline_sports_football_24,
            R.drawable.ic_baseline_sports_basketball_24,
            R.drawable.ic_baseline_sports_cricket_24,
            R.drawable.ic_baseline_sports_mma_24,
            R.drawable.ic_baseline_sports_rugby_24,
            R.drawable.ic_baseline_sports_soccer_24};
    Context context;

    public CustomAdapter(Context context, ArrayList<Sport> sports) {
        this.context = context;
        this.sports = sports;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(sports.get(position).getName());
        holder.image.setImageResource(sportsImages[position]);
    }

    @Override
    public int getItemCount() {
        return sports.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            JSONObject jsonObject = null;
            try {
                jsonObject = getDetailsJson(getAdapterPosition());
            } catch (MalformedURLException | ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup_details, null);

            TextView textViewSportName = popupView.findViewById(R.id.sportName);
            TextView textViewAddress = popupView.findViewById(R.id.address);
            TextView textViewPhone = popupView.findViewById(R.id.phoneNumber);
            TextView textViewPrice = popupView.findViewById(R.id.price);

            try {
                textViewSportName.setText(jsonObject.getString("name"));
                textViewAddress.setText(jsonObject.getString("address"));
                textViewPhone.setText(jsonObject.getString("phone"));
                textViewPrice.setText(jsonObject.getString("price")+" "+jsonObject.getString("currency"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

            popupView.setOnTouchListener((v, event) -> {
                popupWindow.dismiss();
                return true;
            });
        }
    }
    private JSONObject getDetailsJson(int id) throws MalformedURLException, ExecutionException, InterruptedException {
        URL url = new URL("https://engine.free.beeceptor.com/api/getSportDetails?sportId="+(id+1));
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<JSONObject> futureJson = service.submit(()->{
            String json = null;
            try {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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
            return new JSONObject(json);
        });
        return futureJson.get();
    }
}
