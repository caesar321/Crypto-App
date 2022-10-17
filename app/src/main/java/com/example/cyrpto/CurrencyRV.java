package com.example.cyrpto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CurrencyRV extends RecyclerView.Adapter<CurrencyRV.ViewHolder> {
    private ArrayList<CurrencyModel> currencyModel= new ArrayList<>();
    private Context context;
    private DecimalFormat dft = new DecimalFormat("#.##");


    public CurrencyRV(ArrayList<CurrencyModel> currencyModel, Context context) {
        this.currencyModel = currencyModel;
        this.context = context;
    }

    public void filterList(ArrayList<CurrencyModel>filteredList){
        currencyModel=filteredList;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public CurrencyRV.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(context).inflate(R.layout.currency_rv_item,parent,false);
      return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyRV.ViewHolder holder, int position) {
        holder.Symbol.setText(currencyModel.get(position).getSymbol());
        holder.Name.setText(currencyModel.get(position).getName());
        holder.Rate.setText("$"+ dft.format(currencyModel.get(position).getPrice()));

    }

    @Override
    public int getItemCount() {
        return currencyModel.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        private TextView Symbol,Name,Rate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Symbol= itemView.findViewById(R.id.Symbol);
            Name= itemView.findViewById(R.id.name);
            Rate = itemView.findViewById(R.id.rate);
        }
    }
}
