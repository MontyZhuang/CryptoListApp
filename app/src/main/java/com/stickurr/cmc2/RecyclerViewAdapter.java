package com.stickurr.cmc2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mIds;
    private ArrayList<String> mCoinNames;
    private ArrayList<String> mCoinPrices;
    private ArrayList<String> mImages;
    private ArrayList<String> mPercentChange;
    private Context mContext;
    private OnCoinListener monCoinListener;

    public RecyclerViewAdapter(ArrayList<String> mCoinNames,
                               Context mContext,
                               ArrayList<String> mCoinPrices,
                               ArrayList<String> mImages,
                               ArrayList<String> mPercentChange,
                               ArrayList<String> mIds,
                               OnCoinListener monCoinListener) {

        this.mIds = mIds;
        this.mCoinNames = mCoinNames;
        this.mContext = mContext;
        this.mImages = mImages;
        this.mCoinPrices = mCoinPrices;
        this.mPercentChange = mPercentChange;
        this.monCoinListener = monCoinListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem, viewGroup, false);
        ViewHolder holder = new ViewHolder(view, monCoinListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Log.d(TAG, "onBindViewHolder is called");

        viewHolder.CoinName.setText(mCoinNames.get(i));

        Glide.with(mContext)
                .asBitmap()
                .load(mImages.get(i))
                .into(viewHolder.Image);

        viewHolder.CoinPrice.setText(mCoinPrices.get(i));
        viewHolder.CoinRank.setText(Integer.toString(i + 1));

         if (mPercentChange.get(i).indexOf('-') != -1) {
             viewHolder.CoinChange.setText(mPercentChange.get(i).substring(0,5) + "%");
         } else {
             if (mPercentChange.get(i).length() > 3) {
                 viewHolder.CoinChange.setText(mPercentChange.get(i).substring(0, 4) + "%");
             } else {
                 viewHolder.CoinChange.setText(mPercentChange.get(i).substring(0, 3) + "%");
             }


         }
       // viewHolder.CoinChange.setText(mPercentChange.get(i).substring(0,5) + "%");

        int green  = Color.parseColor("#009900");
        if (mPercentChange.get(i).indexOf('-') != -1) {
            viewHolder.CoinChange.setTextColor(Color.RED);
        } else {

            viewHolder.CoinChange.setTextColor(green);
        }



    }

    @Override
    public int getItemCount() {
        return mPercentChange.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView CoinName;
        TextView CoinRank;
        TextView CoinPrice;
        TextView CoinChange;
        ImageView Image;
        LinearLayout parentLayout;

        OnCoinListener onCoinListener;

        public ViewHolder(@NonNull View itemView, OnCoinListener onCoinListener) {
            super(itemView);

            CoinChange = itemView.findViewById(R.id.percentage_change);
            CoinRank = itemView.findViewById(R.id.tv4);
            Image = itemView.findViewById(R.id.image);
            CoinName = itemView.findViewById(R.id.tv2);
            CoinPrice = itemView.findViewById(R.id.tv3);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            this.onCoinListener = onCoinListener;

            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            onCoinListener.OnCoinClick(getAdapterPosition());

        }
    }

    public interface OnCoinListener {
        void OnCoinClick(int position);
    }
}
