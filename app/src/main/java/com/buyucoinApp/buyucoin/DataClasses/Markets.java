package com.buyucoinApp.buyucoin.DataClasses;

public class Markets {
    public String price, value, vol;

    public Markets(String price, String value, String vol){
        this.price = price;
        this.value = value;
        this.vol = vol;
    }

    public String string(){ return this.price + this.value + this.vol;}
}
