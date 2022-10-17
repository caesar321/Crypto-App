package com.example.cyrpto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText searchEdt;
    private RecyclerView Currencies_Recycler_View;
    private ProgressBar progressBar;
    private CurrencyRV currencyRV;
    private ArrayList<CurrencyModel> currencyModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchEdt=findViewById(R.id.txtSearch);
        Currencies_Recycler_View= findViewById(R.id.RvCurrencies_recycler);
        progressBar=findViewById(R.id.progress_bar);
        currencyModels=new ArrayList<>();
        currencyRV= new CurrencyRV(currencyModels,this);
        Currencies_Recycler_View.setLayoutManager( new LinearLayoutManager(this));
        Currencies_Recycler_View.setAdapter(currencyRV);
        getCurrencyData();
        searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filteredCurrencies(s.toString());
            }
        });

    }
    private void filteredCurrencies(String currency){
        ArrayList<CurrencyModel>filteredlist= new ArrayList<>();
        for(CurrencyModel item:currencyModels){
            if(item.getName().toLowerCase().contains(currency.toLowerCase())){
                filteredlist.add(item);

            }
        }
        if(filteredlist.isEmpty()){
            Toast.makeText(this, "No Currency found", Toast.LENGTH_SHORT).show();
        }else{
           currencyRV.filterList(filteredlist);
        }
    }



    private void getCurrencyData(){
        progressBar.setVisibility(View.VISIBLE);
        String url="https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                try{
                    JSONArray dataarray= response.getJSONArray("data");
                    for(int i=0;i<dataarray.length();i++){
                        JSONObject dataobj=dataarray.getJSONObject(i);
                        String name=dataobj.getString("name");
                        String symbol= dataobj.getString("symbol");
                        JSONObject quote=dataobj.getJSONObject("quote");
                        JSONObject USD= quote.getJSONObject("USD");
                        double price= USD.getDouble("price");
                        currencyModels.add(new CurrencyModel(name,symbol,price));

                    }
                    currencyRV.notifyDataSetChanged();

                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Fail to extract json file", Toast.LENGTH_SHORT).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "fail to get the data....", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String>header= new HashMap<>();
                header.put("X-CMC_PRO_API_KEY","c6137525-c4a0-4020-b1a6-e214cd403237");
                return header;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}