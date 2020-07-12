package com.uchiha.greetings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.$Gson$Preconditions;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SecondActivity extends AppCompatActivity {
    List<Country> Countries;
    EditText editText;
    ProgressBar progressBar;
    Country country;
    RecyclerView recyclerView;
    CountryAdapter countryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        Countries = new ArrayList<>();
        checkConnection();
        OkHttpClient client = new OkHttpClient();
        String url = "https://api.covid19api.com/summary";
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    final String myresponse = response.body().string();
                    JsonObject jsonObject = new JsonParser().parse(myresponse).getAsJsonObject();
                    JsonArray jsonElements = jsonObject.getAsJsonArray("Countries");
                    JsonObject object;
                    String count;
                    for(JsonElement element : jsonElements){
                        object = element.getAsJsonObject();
                        count = object.get("Country").toString().replace("\"","");
                        country = new Country(count,object.get("TotalConfirmed").toString(),object.get("TotalDeaths").toString(),object.get("TotalRecovered").toString());
                        Countries.add(country);
                    }
                    if(Countries.size()==0){
                        Toast.makeText(SecondActivity.this, "Unable to get the info try again later", Toast.LENGTH_SHORT).show();
                    }
                    SecondActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            countryAdapter = new CountryAdapter(Countries);
                            recyclerView.setAdapter(countryAdapter);
                        }
                    });
                }
            }
        });
    editText = findViewById(R.id.search);
    editText.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            searching(s.toString());
        }
    });
    }

    private void checkConnection() {
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if(null==activeNetwork){
                Toast.makeText(this, "You kidding with the Internet bro?", Toast.LENGTH_SHORT).show();
            }
    }

    private void searching(String text) {
        ArrayList<Country> filteredList = new ArrayList<>();
        for (Country item : Countries) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        countryAdapter.filterList(filteredList);
    }
}

