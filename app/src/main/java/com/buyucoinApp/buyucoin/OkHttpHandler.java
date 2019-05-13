package com.buyucoinApp.buyucoin;

import com.buyucoinApp.buyucoin.config.Config;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

public class OkHttpHandler {

    private static String BASE_URL = new Config().getAPI_BASE_URL();
    private static OkHttpClient.Builder client = new OkHttpClient().newBuilder().readTimeout(15, TimeUnit.SECONDS).connectTimeout(15,TimeUnit.SECONDS);
    private static MediaType mediaType = MediaType.parse("application/json; charset=utf-8");


    static void post(String url, String content, Callback callback){
        RequestBody body = RequestBody.create(mediaType, content);
        Request request1 = new Request.Builder()
                .url(BASE_URL+url)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();
        Call call = client.build().newCall(request1);
        call.enqueue(callback);
    }

    public static Call auth_post(String url, String access_token, String content, Callback callback){
        RequestBody body = RequestBody.create(mediaType, content);
        Request request1 = new Request.Builder()
                .url(BASE_URL+url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "JWT "+access_token)
                .post(body)
                .build();
        Call call = client.build().newCall(request1);
        call.enqueue(callback);
        return call;
    }

    static void refresh_post(String url , String refresh_token, String content, Callback callback){
        RequestBody body = RequestBody.create(mediaType, content);
        Request request1 = new Request.Builder()
                .url(BASE_URL+url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "JWT "+refresh_token)
                .post(body)
                .build();
        Call call = client.build().newCall(request1);
        call.enqueue(callback);
    }


    public static Call auth_get(String url, String access_token, Callback callback){
        Request request1 = new Request.Builder()
                .url(BASE_URL+url)
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "JWT "+access_token)
                .build();
        Call call = client.build().newCall(request1);
        call.enqueue(callback);
        return call;
    }

//    public static Call get_rates(Callback callback){
//        Request request = new Request.Builder()
//                .url("https://www.buyucoin.com/api/v1.2/currency/markets")
//                .get()
//                .build();
//        Call call = client.build().newCall(request);
//        call.enqueue(callback);
//        return call;
//    }

    public static Call get(String url, Callback callback){
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = client.build().newCall(request);
        call.enqueue(callback);
        return call;
    }

    public static void cancelAllRequests(){
        client.build().dispatcher().cancelAll();
    }
}

