package com.buyucoin.buyucoin.pojos;

import java.util.ArrayList;

public class Ask {
    private Double ask_amount;

    public Ask(Double ask_amount) {
        this.ask_amount = ask_amount;
    }

    public Double getAsk_amount() {
        return ask_amount;
    }


    public  static ArrayList<Ask> randomAsks(){
        ArrayList<Ask> askArrayList = new ArrayList<>();
        for(int i=0;i<=20;i++){
            Ask asks = new Ask(Math.random()*10);
            askArrayList.add(asks);
        }

        return  askArrayList;
    }
}
