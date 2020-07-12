package com.uchiha.greetings;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.uchiha.greetings.R.layout.country;
public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {
    private List<Country> countries;
    private Context context;
    private String userid;
    private OnCountryListener mListener;
    public interface OnCountryListener{
        void OnCountryClick(int position);
    }
    public void setOnCountryClickListener(OnCountryListener onCountryClickListener){
        mListener = onCountryClickListener;
    }
    public  CountryAdapter(List<Country> mcountries){
            this.countries = mcountries;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(country,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        String name = countries.get(position).getName();
        String cases = countries.get(position).getCases();
        String deaths = countries.get(position).getDeaths();
        String recoveries = countries.get(position).getRecovered();
        String deathsd,recoveried;
        int cased = Integer.parseInt(cases);
        String casesd = "Cases : ".concat(NumberFormat.getNumberInstance(Locale.US).format(cased));
        if(deaths.equals("Uk")){
            deathsd = "Deaths : Unknown";
        }
        else{
            int deathd = Integer.parseInt(deaths);

            deathsd = "Deaths : ".concat(NumberFormat.getNumberInstance(Locale.US).format(deathd));

        }
        if(recoveries.equals("Uk")){
            recoveried = "Recoveries : Unknown";
        }
        else{
            int recoveriesd = Integer.parseInt(recoveries);
            recoveried = "Recoveries : ".concat(NumberFormat.getNumberInstance(Locale.US).format(recoveriesd));
        }

        holder.setData(name,casesd,deathsd,recoveried);
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameview,casesview,deathsview,recsview;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameview = itemView.findViewById(R.id.Name);
            casesview = itemView.findViewById(R.id.cases);
            deathsview = itemView.findViewById(R.id.deaths);
            recsview = itemView.findViewById(R.id.recoveries);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        int pos = getAdapterPosition();
                        if(pos!=RecyclerView.NO_POSITION){
                            mListener.OnCountryClick(pos);
                        }
                    }
                }
            });
        }
        private void setData(String name,String cases,String deaths,String recoveries){
            nameview.setText(name);
            casesview.setText(cases);
            deathsview.setText(deaths);
            recsview.setText(recoveries);
        }
    }
    public void filterList(ArrayList<Country> filteredList){
        countries = filteredList;
        notifyDataSetChanged();
    }
}
