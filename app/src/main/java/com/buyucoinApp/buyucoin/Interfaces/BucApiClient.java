package com.buyucoinApp.buyucoin.Interfaces;

import com.buyucoinApp.buyucoin.pojos.WalletCoinVertical;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BucApiClient {
    @GET("/get_wallet")
    Call<List<WalletCoinVertical>> repoForWallet(
            @Path("wallet") String user
    );

}
