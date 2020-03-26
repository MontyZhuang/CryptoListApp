package com.stickurr.cmc2;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.RestrictTo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.$Gson$Preconditions;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PortfolioActivity extends AppCompatActivity {

    private CoinmarketcapApi coinGeckoApi;

    private RecyclerView recyclerView;
    private PortfolioRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> pIDs = new ArrayList<>();
    Gson g = new Gson();
    private Float price = 0.00f;
    private Float amount = 0.0f;
    private String coinPriceToSend;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_portfolio);

        Button coinviewbtn = findViewById(R.id.CoinviewButton2);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.coingecko.com/api/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();




        // makes the interface we have
        coinGeckoApi = retrofit.create(CoinmarketcapApi.class);

        coinviewbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        String internalData = (read("storage.json"));
        //Toast.makeText(this, internalData, Toast.LENGTH_SHORT).show();
        if (internalData != null) {
            Log.d("DE", internalData.toString());
            JsonArray internalJson = new JsonParser().parse(internalData).getAsJsonArray();
            //String data = internalJson.getAsJsonArray().get(0).getAsJsonObject().get("name").toString();

            initPortfolioRecyclerView(internalJson);

            //Toast.makeText(this, internalJson.toString(), Toast.LENGTH_LONG).show();

        }



        getIncomingIntent();




    }

    public void showAlertDialog(final String id) {

        AlertDialog.Builder builder = new Builder(PortfolioActivity.this, R.style.MyDialogTheme);


        final View customLayout = getLayoutInflater().inflate(R.layout.custom_alertdialog, null);
        builder.setView(customLayout);
        final EditText priceEnter = customLayout.findViewById(R.id.inputPrice);
        final EditText amountEnter = customLayout.findViewById(R.id.inputAmount);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                price = Float.parseFloat(priceEnter.getText().toString());
                amount = Float.parseFloat(amountEnter.getText().toString());
                Toast.makeText(PortfolioActivity.this, price.toString(), Toast.LENGTH_SHORT).show();
                writeData(id, price, amount);
            }
        });

        AlertDialog dialog = builder.create();

        dialog.setCancelable(true);
        dialog.show();
    }

    private void getIncomingIntent() {
        if(getIntent().hasExtra("id")) {
            String id = getIntent().getStringExtra("id");
            //Toast.makeText(this, id + "e", Toast.LENGTH_SHORT).show();


            showAlertDialog(id);
//                pIDs.add(id);
//                writeData(id);

        }

    }

    public void writeData(String id, float price, float amount) {
        boolean isFilePresent = isFilePresent(getApplicationContext(), "storage.json");

        JSONObject coinBuyData = new JSONObject();
        try {
            coinBuyData.put("bp", price);
            coinBuyData.put("name", id);
            coinBuyData.put("amount", amount);

;
        } catch (JSONException e) {

        }

        if(isFilePresent) {
            String jsonString = read( "storage.json");
            //Toast.makeText(this, jsonString, Toast.LENGTH_SHORT).show();

            JsonArray internalJson = new JsonParser().parse(jsonString).getAsJsonArray();

            boolean same = false;
            for(int i = 0; i < internalJson.size(); i ++) {
                //Log.d("DE", (internalJson.get(i).getAsJsonObject().get("name").toString()).substring(1,internalJson.get(i).getAsJsonObject().get("name").toString().length() - 1));
                //Toast.makeText(this, (internalJson.get(i).getAsJsonObject().get("name").toString()).substring(1,internalJson.get(i).getAsJsonObject().get("name").toString().length() - 1), Toast.LENGTH_SHORT).show();
                if (id.equals((internalJson.get(i).getAsJsonObject().get("name").toString()).substring(1,internalJson.get(i).getAsJsonObject().get("name").toString().length() - 1))) {
                    same = true;
                    Toast.makeText(this, "someething is the same", Toast.LENGTH_SHORT).show();
                }
            }
            String newToAdd = "";
            if (!(same)) {
                newToAdd = ((jsonString.substring(0,jsonString.length()-1) + "," + coinBuyData.toString() + "]"));

                try {
                    FileOutputStream fos = openFileOutput("storage.json",Context.MODE_PRIVATE);
                    fos.write(newToAdd.toString().getBytes());
                    fos.close();

                    //Toast.makeText(this, "added" + id, Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException fileNotFound) {

                } catch (IOException ioException) {

                }

            }
            //Toast.makeText(this, newToAdd, Toast.LENGTH_SHORT).show();




        } else {
            String FILENAME = "storage.json";
            try {
                FileOutputStream fos = openFileOutput("storage.json",Context.MODE_PRIVATE);
                fos.write(("[" +coinBuyData.toString() + "]").getBytes());
                fos.close();

                //Toast.makeText(this, "added" + id, Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException fileNotFound) {

            } catch (IOException ioException) {

            }
        }

    }

    public boolean isFilePresent(Context context, String fileName) {
        String path = getFilesDir().getAbsolutePath() + "/" + fileName;
        File file = new File(path);
        return file.exists();
    }

    private String read(String fileName) {
        try {
            FileInputStream fis = openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (FileNotFoundException fileNotFound) {
            return null;
        } catch (IOException ioException) {
            return null;
        }
    }

    private void initPortfolioRecyclerView(JsonArray jsonData) {
        ArrayList<String> coinNameArray = new ArrayList<>();
        ArrayList<Float> coinQuantityArray = new ArrayList<>();
        ArrayList<Float> coinBPArray = new ArrayList<>();
        ArrayList<String> coinCurrentPriceArray = new ArrayList<>();


        for(int i = 0; i < jsonData.size(); i++ ) {
            coinNameArray.add(jsonData.getAsJsonArray().get(i).getAsJsonObject().get("name").toString());
            coinQuantityArray.add(Float.parseFloat(jsonData.getAsJsonArray().get(i).getAsJsonObject().get("amount").toString()));
            coinBPArray.add(Float.parseFloat(jsonData.getAsJsonArray().get(i).getAsJsonObject().get("bp").toString()));
            coinCurrentPriceArray.add(getCoinPrice(jsonData.getAsJsonArray().get(i).getAsJsonObject().get("name").toString()));

        }
        Toast.makeText(this, "weve fucking got ehre", Toast.LENGTH_SHORT).show();

        recyclerView = findViewById(R.id.CoinsRecyclerView);

        layoutManager = new GridLayoutManager(this, 2);
        mAdapter = new PortfolioRecyclerAdapter(coinNameArray, coinQuantityArray, coinBPArray, coinCurrentPriceArray);

        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(layoutManager);

    }

    private String getCoinPrice(String id) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("localisation", "false");
        parameters.put("market_data", "true");
        parameters.put("community_data", "false");
        parameters.put("developer_data", "false");


        Call<JsonObject> call = coinGeckoApi.getDetails(id, parameters);


        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(!response.isSuccessful()) {
                    return;
                }
                coinPriceToSend = (response.body().getAsJsonObject().get("market_data").getAsJsonObject().get("current_price").getAsJsonObject().get("usd")).toString();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });

        return coinPriceToSend;

    }
}
