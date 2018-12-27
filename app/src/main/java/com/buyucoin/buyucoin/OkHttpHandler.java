package com.buyucoin.buyucoin;

import java.io.IOException;

import okhttp3.*;

public class OkHttpHandler {
    private static String BASE_URL = "http://test.buyucoin.com/andrios/";
    private static OkHttpClient client = new OkHttpClient();
    private static MediaType mediaType = MediaType.parse("application/json");

    public static Call post(String url, String content, Callback callback){
        RequestBody body = RequestBody.create(mediaType, content);
        Request request1 = new Request.Builder()
                .url(BASE_URL+url)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();
        Call call = client.newCall(request1);
        call.enqueue(callback);
        return call;
    }

    public static Call auth_post(String url, String access_token, String content, Callback callback){
        RequestBody body = RequestBody.create(mediaType, content);
        Request request1 = new Request.Builder()
                .url(BASE_URL+url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "JWT "+access_token)
                .post(body)
                .build();
        Call call = client.newCall(request1);
        call.enqueue(callback);
        return call;
    }

    public static Call refresh_post(String url, String refresh_token, String content, Callback callback){
        RequestBody body = RequestBody.create(mediaType, content);
        Request request1 = new Request.Builder()
                .url(BASE_URL+url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "JWT "+refresh_token)
                .post(body)
                .build();
        Call call = client.newCall(request1);
        call.enqueue(callback);
        return call;
    }


    public static Call auth_get(String url, String access_token, Callback callback){
        Request request1 = new Request.Builder()
                .url(BASE_URL+url)
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "JWT "+access_token)
                .build();
        Call call = client.newCall(request1);
        call.enqueue(callback);
        return call;
    }

    public static Call get_rates(Callback callback){
        Request request = new Request.Builder()
                .url("https://www.buyucoin.com/api/v1.2/currency/markets")
                .get()
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    public static Call get(String url, Callback callback){
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }
}

