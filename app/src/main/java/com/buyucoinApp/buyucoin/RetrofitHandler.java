package com.buyucoinApp.buyucoin;

import android.widget.Toast;

import com.buyucoinApp.buyucoin.config.Config;
import com.buyucoinApp.buyucoin.customDialogs.CoustomToast;
//import com.buyucoinApp.buyucoin.retrofitClients.RetrofitClients;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHandler {

    static GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
    static Gson gson = builder.create();

    private static String BASE_URL = new Config().getAPI_BASE_URL();
    private static OkHttpClient.Builder client = new OkHttpClient().newBuilder().readTimeout(15, TimeUnit.SECONDS).connectTimeout(15,TimeUnit.SECONDS);
    private static MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

    private static Retrofit.Builder RetrofitBuilder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson));

    public static Retrofit retrofit = RetrofitBuilder.build();

    private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Request.Builder newRequest = request.newBuilder()
                            .addHeader("Content-Type", "application/json");

                    return chain.proceed(newRequest.build());
                }
            })
            .connectionSpecs(
                    Arrays.asList(
                            ConnectionSpec.MODERN_TLS,
                            ConnectionSpec.COMPATIBLE_TLS
                    )
            );

    public static  <S> S createAuthService(Class<S> serviceClass, final String access_token){
        if(!httpClientBuilder.interceptors().contains(loggingInterceptor)) {
            httpClientBuilder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Request.Builder newRequest = request.newBuilder()
                            .addHeader("Authorization", "JWT "+access_token);
                    return chain.proceed(newRequest.build());
                }
            });
            RetrofitBuilder = RetrofitBuilder.client(httpClientBuilder.build());
            retrofit = RetrofitBuilder.build();
        }

        return  retrofit.create(serviceClass);
    }
     public static  <S> S createService(Class<S> serviceClass){
        if(!httpClientBuilder.interceptors().contains(loggingInterceptor)) {
            RetrofitBuilder = RetrofitBuilder.client(httpClientBuilder.build());
            retrofit = RetrofitBuilder.build();
        }
        return  retrofit.create(serviceClass);

    }


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

