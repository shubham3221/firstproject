package com.example.s_tools.entertainment.Fragemnts.wallpapers.wallpapersCategory.wallpapers_one.model;

import com.example.s_tools.entertainment.Fragemnts.wallpapers.Fragment_Wallpapers;
import com.example.s_tools.entertainment.Fragemnts.wallpapers.wallpapersCategory.wallpapers_two.Wallpapers_2_Model;
import com.example.s_tools.tools.retrofitcalls.InterceptorRetrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WallpapersApi {
    String wall=Fragment_Wallpapers.wall1;
    String walll2 = Fragment_Wallpapers.wall2;
    Retrofit retrofitWall1=new Retrofit.Builder().baseUrl(wall).client(InterceptorRetrofit.getInterceptor()).addConverterFactory(GsonConverterFactory.create()).build();
    Retrofit retrofitWall2=new Retrofit.Builder().baseUrl(walll2).client(InterceptorRetrofit.getInterceptor()).addConverterFactory(GsonConverterFactory.create()).build();

    @GET("api.php?limit=24&action=previews&type=ico_v/")
    Call<WallpapersModel> wall1apicall(@Query("category") int categories, @Query("page") int page);

    @GET("fetchpopularwallpapers.php?type=NoBabe&filter=downloads")
    Call<List<Wallpapers_2_Model>> fetchPopular(@Query("offset") int offset);
    @GET("fetchrecentwallpapers.php?type=NoBabe&what=recent")
    Call<List<Wallpapers_2_Model>> fetchRecent(@Query("offset") int offset);
    @GET("fetchcategorieswallpapers.php?what=recent")
    Call<List<Wallpapers_2_Model>> fetchCategoryWalls(@Query("type") String type, @Query("offset") int offset);
}

//https://www.367labs.a2hosted.com/script/casual/4.0/fetchcategorieswallpapers.php?type=Abstract&offset=0&what=Recent
//https://www.367labs.a2hosted.com/script/casual/4.0/fetchcategorieswallpapers.php?type=Amoled&offset=0&what=Recent
//https://www.367labs.a2hosted.com/script/casual/4.0/fetchcategorieswallpapers.php?type=Animal&offset=0&what=Recent
//https://www.367labs.a2hosted.com/script/casual/4.0/fetchcategorieswallpapers.php?type=Cartoon & Funny&offset=0&what=Recent
//https://www.367labs.a2hosted.com/script/casual/4.0/fetchcategorieswallpapers.php?type=Dark & Horror&offset=0&what=Recent
//https://www.367labs.a2hosted.com/script/casual/4.0/fetchcategorieswallpapers.php?type=Love & Hearts
//https://www.367labs.a2hosted.com/script/casual/4.0/fetchcategorieswallpapers.php?type=Flower&offset=0&what=Recent
//https://www.367labs.a2hosted.com/script/casual/4.0/fetchcategorieswallpapers.php?type=Game&offset=0&what=Recent
//https://www.367labs.a2hosted.com/script/casual/4.0/fetchcategorieswallpapers.php?type=Girl&offset=0&what=Recent
//https://www.367labs.a2hosted.com/script/casual/4.0/fetchcategorieswallpapers.php?type=Minimal&offset=0&what=Recent
//https://www.367labs.a2hosted.com/script/casual/4.0/fetchcategorieswallpapers.php?type=Space&offset=0&what=Recent
//https://www.367labs.a2hosted.com/script/casual/4.0/fetchcategorieswallpapers.php?type=Superhero&offset=0&what=Recent
//https://www.367labs.a2hosted.com/script/casual/4.0/totalwallpapers.php?category=Movie
//https://www.367labs.a2hosted.com/script/casual/4.0/totalwallpapers.php?category=Other