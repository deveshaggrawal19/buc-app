package com.buyucoin.buyucoin.pojos;

import java.util.ArrayList;

public class Bids {
    private Double bid_amount ;

    private Bids(Double bid_amount) {
        this.bid_amount = bid_amount;
    }

    public Double getBid_amount() {
        return bid_amount;
    }

    public  static ArrayList<Bids> randomBids(){
        ArrayList<Bids> bidsArrayList = new ArrayList<>();
        for(int i=0;i<=20;i++){
            Bids bids = new Bids(Math.random()*100);
            bidsArrayList.add(bids);
        }
        return  bidsArrayList;
    }

}
