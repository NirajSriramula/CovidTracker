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
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class IndiaWideActivity extends AppCompatActivity implements CountryAdapter.OnCountryListener{
    List<Country> Countries;
    EditText editText;
    JsonArray jsonElements;
    Country country;
    RecyclerView recyclerView;
    CountryAdapter countryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_india_wide);
        recyclerView = findViewById(R.id.viewitems);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        Countries = new ArrayList<>();
        checkConnection();
        OkHttpClient client = new OkHttpClient();
        String url = "https://api.covidindiatracker.com/state_data.json";
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    String data = response.body().string();
                    jsonElements = new JsonParser().parse(data).getAsJsonArray();
                    String state,recovered,deaths,cases,active,combined;
                    JsonObject object;
                    for(JsonElement element :jsonElements){
                        object = element.getAsJsonObject();
                        state = object.get("state").toString().replace("\"","");
                        recovered = object.get("recovered").toString();
                        cases = object.get("confirmed").toString();
                        deaths = object.get("deaths").toString();
                        if(state.contains("known")){
                            break;
                        }
                        country = new Country(state,cases,deaths,recovered);
                        Countries.add(country);
                    }
                    IndiaWideActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            countryAdapter = new CountryAdapter(Countries);
                            recyclerView.setAdapter(countryAdapter);
                            countryAdapter.setOnCountryClickListener(IndiaWideActivity.this);
                        }
                    });

                }
            }
        });
        editText = findViewById(R.id.searchbro);
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

    @Override
    public void OnCountryClick(int position) {
        Countries.clear();
        JsonObject state = jsonElements.get(position).getAsJsonObject();
        JsonArray array = state.get("districtData").getAsJsonArray();
        for( JsonElement ele : array){
            JsonObject obj = ele.getAsJsonObject();
            String confirmed,deaths,recovered;
            confirmed = obj.get("confirmed").toString();
            deaths = obj.get("deaths").toString();
            recovered = obj.get("recovered").toString();
            if(confirmed.equals("null")){
                confirmed = "Uk";
            }
            if(deaths.equals("null")){
                deaths = "Uk";
            }
            if(recovered.equals("null")){
                recovered = "Uk";
            }
            Country district = new Country(obj.get("name").toString().replace("\"",""),confirmed,deaths,recovered);
            Countries.add(district);
        }
        CountryAdapter coutryAdapter = new CountryAdapter(Countries);
        recyclerView.setAdapter(coutryAdapter);
    }
}
