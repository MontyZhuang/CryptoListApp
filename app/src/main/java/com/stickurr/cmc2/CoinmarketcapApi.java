package com.stickurr.cmc2;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface CoinmarketcapApi {


    //@Headers("X-CMC_PRO_API_KEY : d6d5c05a-df75-4b22-af92-641b32dd306a")
    @GET("coins/markets")
    Call<JsonArray> getCoins (
          @QueryMap Map<String, String> parameters

    );

    @GET("coins/{id}")
    Call<JsonObject> getDetails (
            @Path("id") String id,
            @QueryMap Map<String, String> parameters
    );
}
