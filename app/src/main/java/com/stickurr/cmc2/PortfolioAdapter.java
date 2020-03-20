package com.stickurr.cmc2;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stickurr.cmc2.RecyclerViewAdapter.ViewHolder;

public class PortfolioAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout parentLayout;
        TextView coinName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            coinName = itemView.findViewById(R.id.CoinName);
            parentLayout = itemView.findViewById(R.id.CoinsRecyclerView);
        }
    }
}
