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
        for(int i=0;i<=5;i++){
            Bids bids = new Bids((Math.random()*2)+1);
            bidsArrayList.add(bids);
        }
        return  bidsArrayList;
    }

}
