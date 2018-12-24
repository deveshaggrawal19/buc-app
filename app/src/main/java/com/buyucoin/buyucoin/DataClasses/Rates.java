package com.buyucoin.buyucoin.DataClasses;

public class Rates {
    public String bid, ask, last_trade, change, currency;

    public Rates(String bid, String ask, String last_trade, String change, String currency){
        this.bid = bid;
        this.ask = ask;
        this.last_trade = last_trade;
        this.change = change;
        this.currency = currency;
    }

    public String string(){
        return currency + bid + ask + last_trade + change;
    }
}