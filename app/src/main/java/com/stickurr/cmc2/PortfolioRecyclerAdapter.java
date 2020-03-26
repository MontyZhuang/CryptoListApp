package com.stickurr.cmc2;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class PortfolioRecyclerAdapter extends RecyclerView.Adapter<PortfolioRecyclerAdapter.ViewHolder> {

    private ArrayList<String> mCoinNames = new ArrayList<>();
    private ArrayList<Float> mCoinQuants = new ArrayList<>();
    private ArrayList<Float> mCoinBPs = new ArrayList<>();
    private ArrayList<String> mCoinCurrPrice = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_portfolio_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.coinName.setText(mCoinNames.get(position));
        viewHolder.coinTotalValue.setText(String.valueOf(mCoinQuants.get(position) * 2.0f));

    }

    @Override
    public int getItemCount() {
        return mCoinNames.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView coinName;
        TextView coinTotalProfit;
        TextView coinTotalValue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            coinName = itemView.findViewById(R.id.portfolioCoinName);
            coinTotalProfit = itemView.findViewById(R.id.portfolioTotalProfitv);
            coinTotalValue = itemView.findViewById(R.id.portfolioTotalValuev);
        }
    }

    public PortfolioRecyclerAdapter(ArrayList<String> coinNames,
                                    ArrayList<Float> coinQuants,
                                    ArrayList<Float> coinBPs,
                                    ArrayList<String> coinCurrPrice) {
        mCoinNames = coinNames;
        mCoinQuants = coinQuants;
        mCoinBPs = coinBPs;
        mCoinCurrPrice = coinCurrPrice;

    }


}
