package com.stickurr.cmc2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class PortfolioActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter mAdapater;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> pIDs = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_portfolio);
        recyclerView = findViewById(R.id.CoinsRecyclerView);

        Button coinviewbtn = findViewById(R.id.CoinviewButton2);

        coinviewbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        getIncomingIntent();

    }

    private void getIncomingIntent() {
        if(getIntent().hasExtra("id")) {
            String id = getIntent().getStringExtra("id");
            //Toast.makeText(this, id + "e", Toast.LENGTH_SHORT).show();
            pIDs.add(id);
            writeData(id);
        }

    }

    public void writeData(String id) {
        boolean isFilePresent = isFilePresent(getApplicationContext(), "storage.txt");
        if(isFilePresent) {
            String jsonString = read(getApplicationContext(), "storage.txt");
            Toast.makeText(this, jsonString, Toast.LENGTH_SHORT).show();
        } else {
            String FILENAME = "storage.txt";
            try {
                String coinname = id + ",";
                FileOutputStream fos = openFileOutput("storage.txt",Context.MODE_PRIVATE);
                fos.write(coinname.getBytes());
                fos.close();

                Toast.makeText(this, "added" + id, Toast.LENGTH_SHORT).show();
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


    private String read(Context context, String fileName) {
        try {
            FileInputStream fis = context.openFileInput(fileName);
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

}
