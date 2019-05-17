package com.buyucoinApp.buyucoin;

import android.content.Context;

import com.buyucoinApp.buyucoin.retrofitClients.RetrofitClients;
import com.buyucoinApp.buyucoin.retrofitRepos.error.ApiError;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class RetrofitErrorHelper {
    public static ApiError parseError(Response<?> response){
        Converter<ResponseBody,ApiError> converter = RetrofitHandler.retrofit.responseBodyConverter(ApiError.class,new Annotation[0]);
        ApiError error;

        try{
            error = converter.convert(response.errorBody());
        }catch (IOException e){
            return new ApiError();
        }

        return error;
    }
}
