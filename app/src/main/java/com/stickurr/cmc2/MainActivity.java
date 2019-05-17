package com.stickurr.cmc2;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.stickurr.cmc2.RecyclerViewAdapter.OnCoinListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements OnCoinListener, OnRefreshListener {

    // create an instance of the API interface
    private CoinmarketcapApi coinmarketcapApi;

    private SwipeRefreshLayout swipeRefreshLayout;

    // initiate all the array that hold the information for the list
    private ArrayList<String> mCoinPercentChange = new ArrayList<>();
    private ArrayList<String> mCoins = new ArrayList<>();
    private ArrayList<String> mCoinPrices = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> mCoinRanks = new ArrayList<>();
    private ArrayList<String> mIDs = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout = findViewById(R.id.swipe);

        swipeRefreshLayout.setOnRefreshListener(this);

        //textView = findViewById(R.id.test);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.coingecko.com/api/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // makes the interface we have
        coinmarketcapApi = retrofit.create(CoinmarketcapApi.class);


        //getCoins();
        //getCoinData();
        getPosts(1);

        //initCoins();
    }

    private void getPosts(final int page) {


        Map<String, String> parameters = new HashMap<>();
        parameters.put("vs_currency", "usd");
        parameters.put("order", "market_cap_desc");
        parameters.put("per_page", "250");
        parameters.put("page", Integer.toString(page));
        //parameters.put("_order", "desc");

        Call<JsonArray> call = coinmarketcapApi.getCoins(parameters);

        // runs it in a background thread
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                // checks if the http response is not one of those 404 ones
                if (!response.isSuccessful()) {
                    // textView.setText("Code:" + response.code());
                    return;
                }

                for (int i = 0; i < 250; i++) {
                    String change = (response.body().get(i).getAsJsonObject().get("price_change_percentage_24h").toString());

                    mCoinPercentChange.add(change);
                    /*if (change.indexOf('-') != -1) {
                        mCoinPercentChange.add(change + "%");
                    } else {
                        mCoinPercentChange.add(change.substring(0, 4) + "%");
                    }*/

                    // ranks


                    // Name of coins
                    String coinname = (response.body().get(i).getAsJsonObject().get("name").toString());
                    coinname = coinname.substring(1, coinname.length() - 1);
                    mCoins.add(coinname);


                    String price = (response.body().get(i).getAsJsonObject().get("current_price").toString());
                    try {
                        mCoinPrices.add("$" + price.substring(0, 6));
                    } catch (Exception e) {
                        mCoinPrices.add("$" + price);
                    }

                    // urls
                    String url = (response.body().get(i).getAsJsonObject().get("image").toString());
                    url = url.substring(1, url.length() - 1);
                    mImageUrls.add(url);

                    //IDs'
                    mIDs.add(response.body().get(i).getAsJsonObject().get("id").toString());

                }



                if (page == 1) {
                    getPosts(2);
                    initRecyclerView();
                } else if (page == 2) {
                    getPosts(3);
                    initRecyclerView();
                } else if (page == 3) {
                    getPosts(4);
                    initRecyclerView();
                }

                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

            }


            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                // textView.setText(t.getMessage());
            }
        });

    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mCoins, this, mCoinPrices, mImageUrls, mCoinPercentChange, mIDs, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void OnCoinClick(int position) {

        Intent intent = new Intent(this, CoinDetailsActivity.class);
        String id = mIDs.get(position).substring(1,mIDs.get(position).length() - 1);
        intent.putExtra("id", id);
        //Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        //Toast.makeText(this, "Hello u have refreshed", Toast.LENGTH_SHORT).show();
        getPosts(1);
    }
}
