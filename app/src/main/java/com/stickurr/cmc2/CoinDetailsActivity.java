package com.stickurr.cmc2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CoinDetailsActivity extends AppCompatActivity {

    private CoinmarketcapApi coinmarketcapApi;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_details);

        Button addBtn = findViewById(R.id.addbtn);

        addBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CoinDetailsActivity.this, "You want to add? yes", Toast.LENGTH_SHORT).show();
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.coingecko.com/api/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        coinmarketcapApi = retrofit.create(CoinmarketcapApi.class);



        getIncomingIntent();
    }

    private void getIncomingIntent() {
        if(getIntent().hasExtra("id")) {
            String id = getIntent().getStringExtra("id");
            //Toast.makeText(this, id + "e", Toast.LENGTH_SHORT).show();
            setInfo(id);
        }

    }

    private void setInfo(String id) {

        final TextView priceText = findViewById(R.id.priceContent);
        final TextView topText = findViewById(R.id.title);
        final TextView priceBTCText = findViewById(R.id.pricebtcContent);
        final TextView marketCapText = findViewById(R.id.marketcapContent);
        final TextView volumeText = findViewById(R.id.volumeContent);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("localisation", "false");
        parameters.put("market_data", "true");
        parameters.put("community_data", "false");
        parameters.put("developer_data", "false");


        Call<JsonObject> call = coinmarketcapApi.getDetails(id, parameters);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(!response.isSuccessful()) {
                    return;
                }
                topText.setText(response.body().getAsJsonObject().get("name").toString().substring(1,response.body().getAsJsonObject().get("name").toString().length() - 1));


                String name = response.body().getAsJsonObject().get("market_data").getAsJsonObject().get("current_price").getAsJsonObject().get("usd").toString();
                if (name.length() > 6) {
                    name = name.substring(0,6);
                }
                priceText.setText("$" + name);



                String pricebtctext = (response.body().getAsJsonObject().get("market_data").getAsJsonObject().get("current_price").getAsJsonObject().get("btc").toString());
                if (pricebtctext.length() > 10) {
                    priceBTCText.setText(pricebtctext.substring(0,10));
                } else {
                    priceBTCText.setText(pricebtctext);
                }

                marketCapText.setText(response.body().getAsJsonObject().get("market_data").getAsJsonObject().get("market_cap").getAsJsonObject().get("usd").toString());

                volumeText.setText(response.body().getAsJsonObject().get("market_data").getAsJsonObject().get("total_volume").getAsJsonObject().get("usd").toString());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


    }
}
