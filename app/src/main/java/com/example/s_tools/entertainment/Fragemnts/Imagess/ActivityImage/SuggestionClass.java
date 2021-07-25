package com.example.s_tools.entertainment.Fragemnts.Imagess.ActivityImage;

import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


class SuggestionClass  {
    public interface ApiCallback {
        void onResponse(boolean success,JsonArray jsonArray);
    }
    public static final String url="https://startpage.com/do/suggest?lang=english&format=json&query=";

    public static void getSuggestions(String query,ApiCallback apiCallback){
        SuggestionsApi suggestionsApi = SuggestionsApi.retrofit.create(SuggestionsApi.class);
        suggestionsApi.getSuggestions(query).enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.body()!=null && !response.body().isJsonNull()){
                    apiCallback.onResponse(true,response.body());
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                apiCallback.onResponse(false,null);
            }
        });
    }
}
