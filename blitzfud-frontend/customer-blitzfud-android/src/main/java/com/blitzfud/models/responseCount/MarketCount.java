package com.blitzfud.models.responseCount;

import com.blitzfud.models.market.Market;

import java.util.ArrayList;

public class MarketCount {
    private int count;
    private ArrayList<Market> markets;

    public MarketCount() {
    }

    public MarketCount(int count, ArrayList<Market> markets) {
        this.count = count;
        this.markets = markets;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<Market> getMarkets() {
        return markets;
    }

    public void setMarkets(ArrayList<Market> markets) {
        this.markets = markets;
    }
}
